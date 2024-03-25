package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.JwtAuthenticationResponse;
import com.oleksa.ecommerce.dto.SignInRequest;
import com.oleksa.ecommerce.dto.SignUpRequest;
import com.oleksa.ecommerce.dto.TokenRefreshRequest;

public interface AuthenticationService {

    JwtAuthenticationResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);

    void signOut();

    JwtAuthenticationResponse refreshToken(TokenRefreshRequest request);
}
