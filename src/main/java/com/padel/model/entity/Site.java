package com.padel.model.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "sites")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Site {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String nom;
    @Column(nullable = false)
    private String adresse;
    @Column(name = "heure_ouverture", nullable = false)
    private LocalTime heureOuverture;
    @Column(name = "heure_fermeture", nullable = false)
    private LocalTime heureFermeture;
    @Column(name = "annee_civile", nullable = false)
    private Integer anneeCivile;
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Terrain> terrains = new ArrayList<>();
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<JourFermeture> joursFermeture = new ArrayList<>();
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Administrateur> administrateurs = new ArrayList<>();
}
