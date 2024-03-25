package com.oleksa.ecommerce.service;

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
    void shouldSendConfirmationEmailSuccessfully_WhenValidUsernameAndEmailAreGiven() {
        String username = "testUser";
        String email = "test@example.com";
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user, false)).thenReturn("token");

        boolean result = emailService.sendConfirmationEmail(username, email);

        assertTrue(result);
        verify(userRepository, times(1)).findByUsername(username);
        verify(jwtService, times(1)).generateToken(user, false);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenUserNotFound() {
        String username = "testUser";
        String email = "test@example.com";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> emailService.sendConfirmationEmail(username, email));

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void shouldReturnFalse_WhenEmailDoesNotMatchUserEmail() {
        String username = "testUser";
        String email = "test@example.com";
        User user = new User();
        user.setUsername(username);
        user.setEmail("differentEmail@example.com");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = emailService.sendConfirmationEmail(username, email);

        assertFalse(result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void shouldThrowUnauthorizedAccessException_WhenUsernameDoesNotMatchUserUsername() {
        String username = "testUser";
        String email = "test@example.com";
        User user = new User();
        user.setUsername("differentUsername");
        user.setEmail(email);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(UnauthorizedAccessException.class, () -> emailService.sendConfirmationEmail(username, email));

        verify(userRepository, times(1)).findByUsername(username);
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
