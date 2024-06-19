package com.oleksa.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oleksa.ecommerce.dto.JwtAuthenticationResponse;
import com.oleksa.ecommerce.dto.SignInRequest;
import com.oleksa.ecommerce.dto.SignUpRequest;
import com.oleksa.ecommerce.dto.TokenRefreshRequest;
import com.oleksa.ecommerce.service.AuthenticationService;
import com.oleksa.ecommerce.service.JwtService;
import com.oleksa.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AuthController.class})
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    private static final String BASE_AUTH_URL = "/api/auth";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserService userService;

    @Test
    void shouldSignUpSuccessfully_WhenValidSignUpRequestIsGiven() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("password");
        JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse("validToken", "Bearer");
        when(authenticationService.signUp(signUpRequest)).thenReturn(expectedResponse);

        mockMvc.perform(post(BASE_AUTH_URL + "/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is(expectedResponse.getAccessToken())));

        verify(authenticationService, times(1)).signUp(signUpRequest);
    }

    @Test
    void shouldSignInSuccessfully_WhenValidSignInRequestIsGiven() throws Exception {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername("testUser");
        signInRequest.setPassword("password");
        JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse("validToken", "Bearer");
        when(authenticationService.signIn(signInRequest)).thenReturn(expectedResponse);

        mockMvc.perform(post(BASE_AUTH_URL + "/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is(expectedResponse.getAccessToken())));

        verify(authenticationService, times(1)).signIn(signInRequest);
    }

    @Test
    void shouldSignOutSuccessfully() throws Exception {
        doNothing().when(authenticationService).signOut();

        mockMvc.perform(post(BASE_AUTH_URL + "/sign-out")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(authenticationService, times(1)).signOut();
    }

    @Test
    void shouldRefreshTokenSuccessfully_WhenValidTokenRefreshRequestIsGiven() throws Exception {
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest();
        tokenRefreshRequest.setRefreshToken("validRefreshToken");
        JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse("validToken", "Bearer");
        when(authenticationService.refreshToken(tokenRefreshRequest)).thenReturn(expectedResponse);

        mockMvc.perform(post(BASE_AUTH_URL + "/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(tokenRefreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is(expectedResponse.getAccessToken())));

        verify(authenticationService, times(1)).refreshToken(tokenRefreshRequest);
    }
}
