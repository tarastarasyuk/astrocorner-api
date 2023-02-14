package com.itzroma.astrocornerapi.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailSender {

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void send(String receiver, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setText(email, true);
            mimeMessageHelper.setTo(receiver);
            mimeMessageHelper.setSubject("Confirm your email!");
            mimeMessageHelper.setFrom("verify@astrocorner.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            throw new IllegalStateException("Fail to send and email", ex);
        }
    }
}
