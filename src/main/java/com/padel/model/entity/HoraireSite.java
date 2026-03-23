package com.padel.model.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
@Entity
@Table(name = "horaires_site")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoraireSite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;
    @Column(nullable = false)
    private Integer annee;
    @Column(nullable = false)
    private LocalTime heureOuverture;
    @Column(nullable = false)
    private LocalTime heureFermeture;
    @Column(nullable = false)
    private Integer dureMatch = 90; // minutes
    @Column(nullable = false)
    private Integer pauseEntreMaches = 15; // minutes
}
