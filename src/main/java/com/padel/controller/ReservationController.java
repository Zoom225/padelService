package com.padel.controller;
import com.padel.model.entity.Reservation;
import com.padel.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Gestion des reservations")
public class ReservationController {
    private final ReservationService reservationService;
    @GetMapping("/user/{matricule}")
    @Operation(summary = "Reservations d un utilisateur")
    public ResponseEntity<List<Reservation>> getByUser(@PathVariable String matricule) {
        return ResponseEntity.ok(reservationService.getReservationsByUser(matricule));
    }
    @PostMapping
    @Operation(summary = "Creer une reservation")
    public ResponseEntity<Reservation> create(@RequestBody Map<String, Object> body) {
        String matricule = (String) body.get("matricule");
        Long terrainId = Long.valueOf(body.get("terrainId").toString());
        LocalDateTime startTime = LocalDateTime.parse(body.get("startTime").toString());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.createReservation(matricule, terrainId, startTime));
    }
    @GetMapping("/disponibilite")
    @Operation(summary = "Verifier disponibilite")
    public ResponseEntity<Map<String, Boolean>> checkAvailability(
            @RequestParam Long terrainId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        boolean ok = reservationService.isSlotAvailable(terrainId,
                LocalDateTime.parse(startTime), LocalDateTime.parse(endTime));
        return ResponseEntity.ok(Map.of("available", ok));
    }
}
