package com.itzroma.astrocornerapi.repository;

import com.itzroma.astrocornerapi.model.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE EmailVerificationToken t " +
            "SET t.confirmedAt = ?2 " +
            "WHERE t.token = ?1")
    int confirmEVT(String token, LocalDateTime confirmedAt);
}
