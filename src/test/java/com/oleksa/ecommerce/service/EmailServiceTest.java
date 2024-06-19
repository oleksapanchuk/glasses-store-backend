package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.entity.Role;
import com.oleksa.ecommerce.entity.User;
import com.oleksa.ecommerce.exception.ResourceNotFoundException;
import com.oleksa.ecommerce.exception.UnauthorizedAccessException;
import com.oleksa.ecommerce.repository.UserRepository;
import com.oleksa.ecommerce.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    public EmailServiceImpl emailService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private JavaMailSender emailSender;

    @Test
    void shouldSendConfirmationEmailSuccessfully_WhenValidEmailIsGiven() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user, false)).thenReturn("token");

        boolean result = emailService.sendConfirmationEmail(email);

        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(jwtService, times(1)).generateToken(user, false);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenUserNotFoundOnConfirmationEmail() {
        String email = "nonExistingEmail@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> emailService.sendConfirmationEmail(email));

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void shouldSendSimpleMessageSuccessfully_WhenValidEmailIsGiven() {
        String to = "test@example.com";
        String subject = "Some Subject";
        String text = "Hello World \n Spring Boot Email";

        boolean result = emailService.sendSimpleMessage(to, subject, text);

        assertTrue(result);
    }

}
