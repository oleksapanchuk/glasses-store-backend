package com.oleksa.ecommerce.service;

public interface EmailService {
    boolean sendConfirmationEmail(String to);

    boolean sendSimpleMessage(String to, String subject, String text);
}