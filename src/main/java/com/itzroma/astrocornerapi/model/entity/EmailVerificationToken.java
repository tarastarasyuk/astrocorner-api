package com.itzroma.astrocornerapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "email_verification_token")
public class EmailVerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public EmailVerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        createdAt = LocalDateTime.now();

        // TODO: 2/14/2023 think how to replace hard-coded expiration time
        expiresAt = createdAt.plus(600_000, ChronoUnit.MILLIS);
    }
}
