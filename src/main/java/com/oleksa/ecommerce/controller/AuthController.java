package com.oleksa.ecommerce.controller;

import com.oleksa.ecommerce.constants.AppConstants;
import com.oleksa.ecommerce.dto.*;
import com.oleksa.ecommerce.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "User registration")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "User authorization")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }

    @Operation(summary = "User sign out")
    @PostMapping("/sign-out")
    public ResponseEntity<ResponseDto> signOut() {
        authenticationService.signOut();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AppConstants.STATUS_200, AppConstants.MESSAGE_200));
    }

    @Operation(summary = "Refresh token update")
    @PostMapping("/refresh-token")
    public JwtAuthenticationResponse refreshToken(@RequestBody @Valid TokenRefreshRequest request) {
        return authenticationService.refreshToken(request);
    }

}
