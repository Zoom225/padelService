package com.padel.model.entity;
import com.padel.model.enums.AdminType;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "administrateurs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Administrateur {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String login;
    @Column(nullable = false)
    private String motDePasse;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String prenom;
    @Enumerated(EnumType.STRING)
    @Column(name = "type_admin", nullable = false)
    private AdminType typeAdmin;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;
}
