package com.padel.model.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "joueur_match")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoueurMatch extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    @Builder.Default
    private Boolean paye = false;
    @Column(nullable = false)
    @Builder.Default
    private Boolean confirme = true;
    @Column(nullable = false)
    @Builder.Default
    private Boolean estOrganisateur = false;
}
