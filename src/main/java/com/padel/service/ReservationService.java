package com.padel.service;
import com.padel.exception.BusinessException;
import com.padel.model.entity.*;
import com.padel.model.enums.*;
import com.padel.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TerrainRepository terrainRepository;
    private final UserRepository userRepository;
    private final FermetureJourRepository fermetureJourRepository;
    private final HoraireSiteRepository horaireSiteRepository;
    public List<Reservation> getReservationsByUser(String matricule) {
        User user = userRepository.findByMatricule(matricule)
                .orElseThrow(() -> new BusinessException("Utilisateur non trouvé: " + matricule));
        return reservationRepository.findByOrganizerId(user.getId());
    }
    @Transactional
    public Reservation createReservation(String matricule, Long terrainId, LocalDateTime startTime) {
        User user = userRepository.findByMatricule(matricule)
                .orElseThrow(() -> new BusinessException("Utilisateur non trouvé: " + matricule));
        Terrain terrain = terrainRepository.findById(terrainId)
                .orElseThrow(() -> new BusinessException("Terrain non trouvé: " + terrainId));
        // Vérification pénalité
        if (user.getPenaltyUntil() != null && user.getPenaltyUntil().isAfter(LocalDate.now())) {
            throw new BusinessException("Vous avez une pénalité active jusqu'au " + user.getPenaltyUntil() +
                    ". Réservation impossible.");
        }
        // Vérification délai selon type utilisateur
        long daysUntilMatch = ChronoUnit.DAYS.between(LocalDate.now(), startTime.toLocalDate());
        switch (user.getUserType()) {
            case GLOBAL -> {
                if (daysUntilMatch > 21)
                    throw new BusinessException("Les membres globaux peuvent réserver au maximum 3 semaines à l'avance");
            }
            case SITE -> {
                if (daysUntilMatch > 14)
                    throw new BusinessException("Les membres du site peuvent réserver au maximum 2 semaines à l'avance");
                if (user.getSite() == null || !user.getSite().getId().equals(terrain.getSite().getId()))
                    throw new BusinessException("Vous ne pouvez réserver que sur votre site");
            }
            case LIBRE -> {
                if (daysUntilMatch > 5)
                    throw new BusinessException("Les membres libres peuvent réserver au maximum 5 jours à l'avance");
            }
        }
        // Vérification jour de fermeture
        List<FermetureJour> fermetures = fermetureJourRepository
                .findFermetureForSiteAndDate(terrain.getSite().getId(), startTime.toLocalDate());
        if (!fermetures.isEmpty()) {
            throw new BusinessException("Le site est fermé le " + startTime.toLocalDate());
        }
        // Vérification horaires du site
        HoraireSite horaire = horaireSiteRepository
                .findBySiteIdAndAnnee(terrain.getSite().getId(), startTime.getYear())
                .orElseThrow(() -> new BusinessException("Aucun horaire configuré pour ce site en " + startTime.getYear()));
        if (startTime.toLocalTime().isBefore(horaire.getHeureOuverture()) ||
                startTime.toLocalTime().isAfter(horaire.getHeureFermeture())) {
            throw new BusinessException("L'heure de réservation est hors des horaires du site (" +
                    horaire.getHeureOuverture() + " - " + horaire.getHeureFermeture() + ")");
        }
        // Calcul heure de fin (1h30)
        LocalDateTime endTime = startTime.plusMinutes(horaire.getDureMatch());
        // Vérification disponibilité
        List<Reservation> conflicts = reservationRepository
                .findConflictingReservations(terrainId, startTime, endTime);
        if (!conflicts.isEmpty()) {
            throw new BusinessException("Ce créneau est déjà réservé sur ce terrain");
        }
        Reservation reservation = Reservation.builder()
                .terrain(terrain)
                .organizer(user)
                .startTime(startTime)
                .endTime(endTime)
                .build();
        return reservationRepository.save(reservation);
    }
    public boolean isSlotAvailable(Long terrainId, LocalDateTime startTime, LocalDateTime endTime) {
        return reservationRepository.findConflictingReservations(terrainId, startTime, endTime).isEmpty();
    }
}
