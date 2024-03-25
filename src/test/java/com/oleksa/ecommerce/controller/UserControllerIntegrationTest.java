package com.oleksa.ecommerce.controller;

import com.oleksa.ecommerce.dto.UserDto;
import com.oleksa.ecommerce.service.UserService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerIntegrationTest {

    @Value("${server.port}")
    private int serverPort;

    @LocalServerPort
    private int localServerPort;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void contextLoads() {
        System.out.println("server.port = " + serverPort);
        System.out.println("local server port = " + localServerPort);
    }

    @Test
    void shouldReturnUserWhenUserIdExists() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .firstName("Oleksa")
                .lastName("Panchuk")
                .email("test@gmail.com")
                .build();
        when(userService.fetchUserById(anyLong())).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.getUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }
}
