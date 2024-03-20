package com.oleksa.ecommerce.service;

public interface EmailService {
    boolean sendConfirmationEmail(String to, String token);

    boolean sendSimpleMessage(String to, String subject, String text);
}