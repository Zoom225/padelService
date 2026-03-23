package com.padel.model.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
@Entity
@Table(name = "fermeture_jours")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class FermetureJour extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;
    @Column(nullable = false)
    private LocalDate dateFermeture;
    @Column(nullable = false)
    private String motif;
    @Column(nullable = false) @Builder.Default
    private Boolean estGlobal = false;
}
