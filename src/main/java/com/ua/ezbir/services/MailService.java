package com.ua.ezbir.services;

public interface MailService {
    void sendVerificationEmail(String email, String token);
}
