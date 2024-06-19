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

    private static final String BASE_USERS_URL = "/api/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserService userService;
    @MockBean
    private EmailService emailService;


    private String email;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .email("test@gmail.com")
                .build();

        email = "test@gmail.com";
        Authentication auth = new TestingAuthenticationToken(email, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void shouldFetchUserSuccessfully_WhenValidUserIdIsGiven() throws Exception {
        Long userId = 1L;
        when(userService.fetchUserById(userId)).thenReturn(userDto);

        mockMvc.perform(get(BASE_USERS_URL + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId().intValue())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService, times(1)).fetchUserById(userId);
    }

    @Test
    void shouldUpdateUserSuccessfully_WhenValidUserDtoIsGiven() throws Exception {
        userDto.setEmail("updateduser@gmail.com");
        when(userService.updateUser(email, userDto)).thenReturn(true);

        mockMvc.perform(patch(BASE_USERS_URL + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(email, userDto);
    }

    @Test
    void shouldUpdatePasswordSuccessfully_WhenValidPasswordUpdateRequestIsGiven() throws Exception {
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
        passwordUpdateRequest.setOldPassword("oldPassword");
        passwordUpdateRequest.setNewPassword("newPassword");
        when(userService.updatePassword(email, passwordUpdateRequest.getOldPassword(), passwordUpdateRequest.getNewPassword())).thenReturn(true);

        mockMvc.perform(put(BASE_USERS_URL + "/update-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(passwordUpdateRequest)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updatePassword(email, passwordUpdateRequest.getOldPassword(), passwordUpdateRequest.getNewPassword());
    }

    @Test
    void shouldSendEmailConfirmationSuccessfully_WhenValidEmailIsGiven() throws Exception {
        String email = "test@example.com";
        when(emailService.sendConfirmationEmail(email)).thenReturn(true);

        mockMvc.perform(get(BASE_USERS_URL + "/send-email-confirmation")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(emailService, times(1)).sendConfirmationEmail(email);
    }

    @Test
    void shouldConfirmAccountSuccessfully_WhenValidTokenIsGiven() throws Exception {
        String token = "validToken";
        when(userService.confirmUserAccount(token)).thenReturn(true);

        mockMvc.perform(get(BASE_USERS_URL + "/confirm-account")
                        .param("token", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).confirmUserAccount(token);
    }
}
