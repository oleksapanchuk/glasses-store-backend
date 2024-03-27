package com.oleksa.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oleksa.ecommerce.dto.UserDto;
import com.oleksa.ecommerce.dto.request.PasswordUpdateRequest;
import com.oleksa.ecommerce.service.EmailService;
import com.oleksa.ecommerce.service.JwtService;
import com.oleksa.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UserController.class})
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserService userService;
    @MockBean
    private EmailService emailService;


    private String username;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .username("testUser")
                .build();

        username = "testUser";
        Authentication auth = new TestingAuthenticationToken(username, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void shouldFetchUserSuccessfully_WhenValidUserIdIsGiven() throws Exception {
        Long userId = 1L;
        when(userService.fetchUserById(userId)).thenReturn(userDto);

        mockMvc.perform(get("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId().intValue())))
                .andExpect(jsonPath("$.username", is(userDto.getUsername())));

        verify(userService, times(1)).fetchUserById(userId);
    }

    @Test
    void shouldUpdateUserSuccessfully_WhenValidUserDtoIsGiven() throws Exception {
        userDto.setUsername("updatedUser");
        when(userService.updateUser(username, userDto)).thenReturn(true);

        mockMvc.perform(patch("/api/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(username, userDto);
    }

    @Test
    void shouldUpdatePasswordSuccessfully_WhenValidPasswordUpdateRequestIsGiven() throws Exception {
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
        passwordUpdateRequest.setOldPassword("oldPassword");
        passwordUpdateRequest.setNewPassword("newPassword");
        when(userService.updatePassword(username, passwordUpdateRequest.getOldPassword(), passwordUpdateRequest.getNewPassword())).thenReturn(true);

        mockMvc.perform(put("/api/users/update-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(passwordUpdateRequest)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updatePassword(username, passwordUpdateRequest.getOldPassword(), passwordUpdateRequest.getNewPassword());
    }

    @Test
    void shouldSendEmailConfirmationSuccessfully_WhenValidEmailIsGiven() throws Exception {
        String email = "test@example.com";
        when(emailService.sendConfirmationEmail(username, email)).thenReturn(true);

        mockMvc.perform(get("/api/users/send-email-confirmation")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(emailService, times(1)).sendConfirmationEmail(username, email);
    }

    @Test
    void shouldConfirmAccountSuccessfully_WhenValidTokenIsGiven() throws Exception {
        String token = "validToken";
        when(userService.confirmUserAccount(token)).thenReturn(true);

        mockMvc.perform(post("/api/users/confirm-account")
                        .param("token", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).confirmUserAccount(token);
    }
}
