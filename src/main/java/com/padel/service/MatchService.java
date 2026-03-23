package com.padel.service;
import com.padel.exception.BusinessException;
import com.padel.model.entity.*;
import com.padel.model.enums.*;
import com.padel.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {
    private final MatchRepository matchRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final JoueurMatchRepository joueurMatchRepository;
    private final PenaliteRepository penaliteRepository;
    public List<Match> getAllPublicMatches() { return matchRepository.findAllPublicMatches(); }
    public List<Match> getPublicMatchesBySite(Long siteId) { return matchRepository.findPublicMatchesBySite(siteId); }
    public List<Match> getMatchesByOrganizer(String matricule) {
        User user = userRepository.findByMatricule(matricule)
                .orElseThrow(() -> new BusinessException("Utilisateur non trouve: " + matricule));
        return matchRepository.findByOrganizerId(user.getId());
    }
    @Transactional
    public Match createMatch(Long reservationId, String matricule, MatchType matchType) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException("Reservation non trouvee: " + reservationId));
        User organizer = userRepository.findByMatricule(matricule)
                .orElseThrow(() -> new BusinessException("Utilisateur non trouve: " + matricule));
        if (reservation.getMatch() != null)
            throw new BusinessException("Un match existe deja pour cette reservation");
        Match match = Match.builder()
                .reservation(reservation).organizer(organizer)
                .matchType(matchType).status(MatchStatus.PENDING)
                .totalPrice(60.0).pricePerPlayer(15.0).build();
        Match saved = matchRepository.save(match);
        joueurMatchRepository.save(JoueurMatch.builder()
                .match(saved).user(organizer)
                .estOrganisateur(true).paye(false).confirme(true).build());
        return saved;
    }
    @Transactional
    public Match addPlayerToPrivateMatch(Long matchId, String organizerMatricule, String playerMatricule) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new BusinessException("Match non trouve: " + matchId));
        if (match.getMatchType() != MatchType.PRIVATE)
            throw new BusinessException("Ce match n'est pas prive");
        if (!match.getOrganizer().getMatricule().equals(organizerMatricule))
            throw new BusinessException("Seul l'organisateur peut ajouter des joueurs");
        long count = joueurMatchRepository.countByMatchId(matchId);
        if (count >= 4) throw new BusinessException("Le match est complet");
        User player = userRepository.findByMatricule(playerMatricule)
                .orElseThrow(() -> new BusinessException("Joueur non trouve: " + playerMatricule));
        joueurMatchRepository.save(JoueurMatch.builder()
                .match(match).user(player).estOrganisateur(false).paye(false).confirme(true).build());
        if (count + 1 == 4) { match.setStatus(MatchStatus.CONFIRMED); matchRepository.save(match); }
        return match;
    }
    @Transactional
    public Match convertToPublic(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new BusinessException("Match non trouve: " + matchId));
        match.setMatchType(MatchType.PUBLIC);
        return matchRepository.save(match);
    }
    @Transactional
    public void checkAndConvertPrivateMatchesVeille() {
        List<Match> pending = matchRepository.findByMatchTypeAndStatus(MatchType.PRIVATE, MatchStatus.PENDING);
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        for (Match match : pending) {
            if (match.getReservation().getStartTime().toLocalDate().equals(tomorrow)) {
                long count = joueurMatchRepository.countByMatchId(match.getId());
                if (count < 4) {
                    match.setMatchType(MatchType.PUBLIC);
                    matchRepository.save(match);
                    User org = match.getOrganizer();
                    org.setPenaltyUntil(LocalDate.now().plusWeeks(1));
                    userRepository.save(org);
                    penaliteRepository.save(Penalite.builder()
                            .user(org).match(match)
                            .dateDebut(LocalDate.now()).dateFin(LocalDate.now().plusWeeks(1))
                            .motif("Match prive incomplet la veille").build());
                }
            }
        }
    }
}
