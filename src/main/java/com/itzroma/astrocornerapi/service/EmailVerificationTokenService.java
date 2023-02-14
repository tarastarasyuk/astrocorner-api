package com.itzroma.astrocornerapi.service;

import com.itzroma.astrocornerapi.exception.EVTConfirmedException;
import com.itzroma.astrocornerapi.exception.EVTExpiredException;
import com.itzroma.astrocornerapi.exception.EntityNotFoundException;
import com.itzroma.astrocornerapi.model.entity.EmailVerificationToken;
import com.itzroma.astrocornerapi.model.entity.User;
import com.itzroma.astrocornerapi.repository.EmailVerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    private final UserService userService;

    public EmailVerificationToken findByToken(String token) {
        return emailVerificationTokenRepository.findByToken(token).orElseThrow(() -> {
            throw new EntityNotFoundException("Email verification token not found");
        });
    }

    public EmailVerificationToken generateEmailVerificationToken(User user) {
        return emailVerificationTokenRepository.save(new EmailVerificationToken(UUID.randomUUID().toString(), user));
    }

    public boolean validateEmailVerificationToken(String token) {
        EmailVerificationToken emailVerificationToken = findByToken(token);

        if (nonNull(emailVerificationToken.getConfirmedAt())) throw new EVTConfirmedException();
        if (emailVerificationToken.getExpiresAt().isBefore(LocalDateTime.now())) throw new EVTExpiredException();

        emailVerificationTokenRepository.confirmEVT(token, LocalDateTime.now());
        userService.enableUser(emailVerificationToken.getUser());
        return true;
    }

    public EmailVerificationToken regenerateEmailVerificationToken(String old) {
        EmailVerificationToken evt = findByToken(old);

        if (nonNull(evt.getConfirmedAt())) throw new EVTConfirmedException();

        evt.setToken(UUID.randomUUID().toString());
        emailVerificationTokenRepository.save(evt);
        return evt;
    }
}
