package com.padel.controller;
import com.padel.model.entity.Paiement;
import com.padel.service.PaiementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
@Tag(name = "Paiements", description = "Gestion des paiements")
public class PaiementController {
    private final PaiementService paiementService;
    @GetMapping("/match/{matchId}")
    @Operation(summary = "Paiements d un match")
    public ResponseEntity<List<Paiement>> getByMatch(@PathVariable Long matchId) {
        return ResponseEntity.ok(paiementService.getPaiementsByMatch(matchId));
    }
    @GetMapping("/user/{matricule}")
    @Operation(summary = "Paiements d un utilisateur")
    public ResponseEntity<List<Paiement>> getByUser(@PathVariable String matricule) {
        return ResponseEntity.ok(paiementService.getPaiementsByUser(matricule));
    }
    @PostMapping
    @Operation(summary = "Effectuer un paiement")
    public ResponseEntity<Paiement> pay(@RequestBody Map<String, Object> body) {
        String matricule = (String) body.get("matricule");
        Long matchId = Long.valueOf(body.get("matchId").toString());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paiementService.payerMatch(matricule, matchId));
    }
    @GetMapping("/revenue/global")
    @Operation(summary = "Chiffre d affaires global")
    public ResponseEntity<Map<String, Double>> getRevenueGlobal() {
        return ResponseEntity.ok(Map.of("revenue", paiementService.getRevenueGlobal()));
    }
    @GetMapping("/revenue/site/{siteId}")
    @Operation(summary = "Chiffre d affaires par site")
    public ResponseEntity<Map<String, Double>> getRevenueBySite(@PathVariable Long siteId) {
        return ResponseEntity.ok(Map.of("revenue", paiementService.getRevenueBySite(siteId)));
    }
}
