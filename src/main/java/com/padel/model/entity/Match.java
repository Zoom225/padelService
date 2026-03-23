package com.padel.model.entity;
import com.padel.model.enums.MatchStatus;
import com.padel.model.enums.MatchType;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchType matchType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private MatchStatus status = MatchStatus.PENDING;
    @Column(nullable = false) @Builder.Default
    private Double totalPrice = 60.0;
    @Column(nullable = false) @Builder.Default
    private Double pricePerPlayer = 15.0;
    @Column(nullable = false) @Builder.Default
    private Double soldeOrganisateur = 0.0;
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<JoueurMatch> joueurs = new HashSet<>();
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Paiement> paiements = new HashSet<>();
}
