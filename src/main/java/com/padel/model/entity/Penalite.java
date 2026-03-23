package com.padel.model.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
@Entity
@Table(name = "penalites")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Penalite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;
    @Column(nullable = false)
    private LocalDate dateDebut;
    @Column(nullable = false)
    private LocalDate dateFin;
    @Column(nullable = false)
    private String motif;
}
