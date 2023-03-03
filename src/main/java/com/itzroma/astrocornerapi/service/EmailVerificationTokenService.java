package com.itzroma.astrocornerapi.service;

import com.itzroma.astrocornerapi.model.entity.EmailVerificationToken;
import com.itzroma.astrocornerapi.model.entity.User;

public interface EmailVerificationTokenService {

    EmailVerificationToken findByToken(String token);

    EmailVerificationToken generateEmailVerificationToken(User user);

    EmailVerificationToken regenerateEmailVerificationToken(String old);

    boolean validateEmailVerificationToken(String token);

    void sendEmailRegistrationToken(User user, EmailVerificationToken evt);

    void resendEmailVerificationToken(String old);

}
