package com.oleksa.ecommerce.service.impl;

import com.oleksa.ecommerce.dto.JwtAuthenticationResponse;
import com.oleksa.ecommerce.dto.SignInRequest;
import com.oleksa.ecommerce.dto.SignUpRequest;
import com.oleksa.ecommerce.dto.TokenRefreshRequest;
import com.oleksa.ecommerce.entity.Role;
import com.oleksa.ecommerce.entity.User;
import com.oleksa.ecommerce.service.AuthenticationService;
import com.oleksa.ecommerce.service.JwtService;
import com.oleksa.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * User registration
     *
     * @param request user data
     * @return token
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);

        var accessToken = jwtService.generateToken(user, false);
        var refreshToken = jwtService.generateToken(user, true);
        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    /**
     * User authentication
     *
     * @param request user data
     * @return token
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var accessToken = jwtService.generateToken(user, false);
        var refreshToken = jwtService.generateToken(user, true);
        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    /**
     * Refreshing the token
     *
     * @param request refresh token
     * @return new access token
     */
    public JwtAuthenticationResponse refreshToken(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        var username = jwtService.extractUserName(refreshToken);

        UserDetails userDetails = userService
                .userDetailsService()
                .loadUserByUsername(username);

        // Validate the refresh token
        if (jwtService.isTokenValid(refreshToken, userDetails)) {

            // Generate a new access token
            String newAccessToken = jwtService.generateToken(userDetails, false);

            // Generate a new refresh token
            String newRefreshToken = jwtService.generateToken(userDetails, true);

            // Return the new access token
            return new JwtAuthenticationResponse(newAccessToken, newRefreshToken);
        } else {
            throw new IllegalArgumentException("Invalid refresh token");
        }

    }

    /**
     * Generates a new access token for a given refresh token request.
     *
     * @param request The refresh token request containing the refresh token.
     * @return A JwtAuthenticationResponse containing the new access token and the original refresh token.
     * @throws IllegalArgumentException If the refresh token is invalid.
     */
    @Override
    public JwtAuthenticationResponse accessToken(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        var username = jwtService.extractUserName(refreshToken);

        UserDetails userDetails = userService
                .userDetailsService()
                .loadUserByUsername(username);

        // Validate the refresh token
        if (jwtService.isTokenValid(refreshToken, userDetails)) {

            // Generate a new access token
            String newAccessToken = jwtService.generateToken(userDetails, false);

            // Return the new access token
            return new JwtAuthenticationResponse(newAccessToken, refreshToken);
        } else {
            throw new IllegalArgumentException("Invalid refresh token");
        }
    }
}
