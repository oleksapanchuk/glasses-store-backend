package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.JwtAuthenticationResponse;
import com.oleksa.ecommerce.dto.SignInRequest;
import com.oleksa.ecommerce.dto.SignUpRequest;
import com.oleksa.ecommerce.dto.TokenRefreshRequest;
import com.oleksa.ecommerce.entity.User;
import com.oleksa.ecommerce.repository.UserRepository;
import com.oleksa.ecommerce.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserService userService;
    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(userService.userDetailsService()).thenReturn(userDetailsService);
    }

    @Test
    public void whenSignIn_thenReturnsJwtAuthenticationResponse() {
        // Arrange
        SignInRequest request = new SignInRequest();
        request.setUsername("testUser");
        request.setPassword("testPassword");

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(testUser);
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtService.generateToken(any(UserDetails.class), anyBoolean())).thenReturn("testToken");

        // Act
        JwtAuthenticationResponse response = authenticationService.signIn(request);

        // Assert
        assertEquals("testToken", response.getAccessToken());
        assertEquals("testToken", response.getRefreshToken());
    }

    @Test
    public void whenSignUp_thenReturnsJwtAuthenticationResponse() {
        // Arrange
        SignUpRequest request = new SignUpRequest();
        request.setUsername("testUser");
        request.setEmail("testEmail");
        request.setPassword("testPassword");

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("testEmail");
        testUser.setPassword("testPassword");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        doNothing().when(userService).createUser(any(User.class));
        when(jwtService.generateToken(any(User.class), anyBoolean())).thenReturn("testToken");

        // Act
        JwtAuthenticationResponse response = authenticationService.signUp(request);

        // Assert
        assertEquals("testToken", response.getAccessToken());
        assertEquals("testToken", response.getRefreshToken());
    }

    @Test
    public void whenSignOut_thenSecurityContextShouldBeCleared() {
        // Arrange
        SecurityContext securityContext = SecurityContextHolder.getContext();

        // Act
        authenticationService.signOut();

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void whenRefreshToken_thenReturnsJwtAuthenticationResponse() {
        // Arrange
        TokenRefreshRequest request = new TokenRefreshRequest();
        request.setRefreshToken("testRefreshToken");

        UserDetails userDetails = new User();

        when(jwtService.extractEmail(anyString())).thenReturn("testUser");
        when(userService.userDetailsService().loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtService.generateToken(any(UserDetails.class), anyBoolean())).thenReturn("newAccessToken");

        // Act
        JwtAuthenticationResponse response = authenticationService.refreshToken(request);

        // Assert
        assertEquals("newAccessToken", response.getAccessToken());
        assertEquals("testRefreshToken", response.getRefreshToken());
    }

    @Test
    public void whenRefreshTokenIsInvalid_thenThrowsIllegalArgumentException() {
        // Arrange
        TokenRefreshRequest request = new TokenRefreshRequest();
        request.setRefreshToken("invalidRefreshToken");

        UserDetails userDetails = new User();

        when(jwtService.extractEmail(anyString())).thenReturn("testUser");
        when(userService.userDetailsService().loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(false);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.refreshToken(request);
        });
    }

}
