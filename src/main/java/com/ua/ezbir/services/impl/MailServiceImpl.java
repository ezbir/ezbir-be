package com.ua.ezbir.services.impl;

import com.ua.ezbir.services.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendVerificationEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[Є-Збір] Будь ласка, підтвердіть вашу електронну адресу");
        message.setText("""
                Дякуємо за регістрацію на Є-Збір! Код для підтвердження:\s
                """ + token);
        javaMailSender.send(message);
    }
}
