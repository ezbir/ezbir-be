package com.ua.ezbir.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "verification_tokens")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Builder.Default
    @Column(name = "expiry_date")
    private Instant expiryDate = Instant.now().plus(5, ChronoUnit.MINUTES);

    @OneToOne
    private User user;
}
