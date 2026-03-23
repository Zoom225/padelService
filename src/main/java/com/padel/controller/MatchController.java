package com.padel.controller;
import com.padel.model.entity.Match;
import com.padel.model.enums.MatchType;
import com.padel.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
@Tag(name = "Matches", description = "Gestion des matches")
public class MatchController {
    private final MatchService matchService;
    @GetMapping("/public")
    @Operation(summary = "Tous les matches publics")
    public ResponseEntity<List<Match>> getAllPublic() { return ResponseEntity.ok(matchService.getAllPublicMatches()); }
    @GetMapping("/public/site/{siteId}")
    @Operation(summary = "Matches publics par site")
    public ResponseEntity<List<Match>> getPublicBySite(@PathVariable Long siteId) {
        return ResponseEntity.ok(matchService.getPublicMatchesBySite(siteId));
    }
    @GetMapping("/organizer/{matricule}")
    @Operation(summary = "Matches par organisateur")
    public ResponseEntity<List<Match>> getByOrganizer(@PathVariable String matricule) {
        return ResponseEntity.ok(matchService.getMatchesByOrganizer(matricule));
    }
    @PostMapping
    @Operation(summary = "Creer un match")
    public ResponseEntity<Match> createMatch(@RequestBody Map<String, Object> body) {
        Long reservationId = Long.valueOf(body.get("reservationId").toString());
        String matricule = (String) body.get("matricule");
        MatchType type = MatchType.valueOf(body.get("matchType").toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(matchService.createMatch(reservationId, matricule, type));
    }
    @PostMapping("/{matchId}/players")
    @Operation(summary = "Ajouter joueur (match prive)")
    public ResponseEntity<Match> addPlayer(@PathVariable Long matchId, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(matchService.addPlayerToPrivateMatch(
                matchId, body.get("organizerMatricule"), body.get("playerMatricule")));
    }
    @PatchMapping("/{matchId}/convert-public")
    @Operation(summary = "Convertir en match public")
    public ResponseEntity<Match> convertToPublic(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.convertToPublic(matchId));
    }
}
