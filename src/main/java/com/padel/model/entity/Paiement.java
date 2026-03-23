package com.padel.model.entity;
import com.padel.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "paiements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paiement extends BaseEntity {
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
    private Double montant;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;
    private LocalDateTime datePaiement;
    @Column(nullable = false)
    @Builder.Default
    private Boolean estSolde = false;
}
