package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.JwtAuthenticationResponse;
import com.oleksa.ecommerce.dto.SignInRequest;
import com.oleksa.ecommerce.dto.SignUpRequest;
import com.oleksa.ecommerce.dto.TokenRefreshRequest;
import com.oleksa.ecommerce.exception.TokenExpiredException;

public interface AuthenticationService {

    JwtAuthenticationResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);

    JwtAuthenticationResponse refreshToken(TokenRefreshRequest request);
}
