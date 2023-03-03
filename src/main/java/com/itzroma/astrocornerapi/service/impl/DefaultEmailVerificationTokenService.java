package com.itzroma.astrocornerapi.service.impl;

import com.itzroma.astrocornerapi.email.event.EmailVerificationEvent;
import com.itzroma.astrocornerapi.exception.EVTConfirmedException;
import com.itzroma.astrocornerapi.exception.EVTExpiredException;
import com.itzroma.astrocornerapi.exception.EntityNotFoundException;
import com.itzroma.astrocornerapi.model.entity.EmailVerificationToken;
import com.itzroma.astrocornerapi.model.entity.User;
import com.itzroma.astrocornerapi.repository.EmailVerificationTokenRepository;
import com.itzroma.astrocornerapi.service.EmailVerificationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultEmailVerificationTokenService implements EmailVerificationTokenService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final DefaultUserService defaultUserService;
    private final ApplicationEventPublisher publisher;
    @Value("${app.base-url}")
    private String applicationUrl;

    @Override
    public EmailVerificationToken findByToken(String token) {
        return emailVerificationTokenRepository.findByToken(token).orElseThrow(() -> {
            throw new EntityNotFoundException("Email verification token not found");
        });
    }

    @Override
    public EmailVerificationToken generateEmailVerificationToken(User user) {
        return emailVerificationTokenRepository.save(new EmailVerificationToken(UUID.randomUUID().toString(), user));
    }

    @Override
    public EmailVerificationToken regenerateEmailVerificationToken(String old) {
        EmailVerificationToken evt = findByToken(old);

        if (nonNull(evt.getConfirmedAt())) throw new EVTConfirmedException();

        evt.setToken(UUID.randomUUID().toString());
        emailVerificationTokenRepository.save(evt);
        return evt;
    }

    @Override
    public boolean validateEmailVerificationToken(String token) {
        EmailVerificationToken emailVerificationToken = findByToken(token);

        if (nonNull(emailVerificationToken.getConfirmedAt())) throw new EVTConfirmedException();
        if (emailVerificationToken.getExpiresAt().isBefore(LocalDateTime.now())) throw new EVTExpiredException();

        emailVerificationTokenRepository.confirmEVT(token, LocalDateTime.now());
        defaultUserService.enableUser(emailVerificationToken.getUser());
        return true;
    }

    @Override
    public void sendEmailRegistrationToken(User user, EmailVerificationToken evt) {
        log.debug("The email registration token sending was triggered for user " + user.getEmail());
        publisher.publishEvent(new EmailVerificationEvent(user, applicationUrl, evt));
    }

    @Override
    public void resendEmailVerificationToken(String old) {
        EmailVerificationToken evt = regenerateEmailVerificationToken(old);
        sendEmailRegistrationToken(evt.getUser(), evt);
    }

}
