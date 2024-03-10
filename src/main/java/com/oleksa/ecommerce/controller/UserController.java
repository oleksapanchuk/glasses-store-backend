package com.oleksa.ecommerce.controller;

import com.oleksa.ecommerce.dto.UserDto;
import com.oleksa.ecommerce.entity.Product;
import com.oleksa.ecommerce.service.AuthenticationService;
import com.oleksa.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Controller")
public class UserController {

    private final UserService userService;

    @GetMapping("/{user-id}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable(name = "user-id") Long userId
    ) {

        UserDto user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("error on user fetching"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }
}
