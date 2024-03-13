package com.oleksa.ecommerce.controller;

import com.oleksa.ecommerce.dto.UserDto;
import com.oleksa.ecommerce.dto.request.PasswordUpdateRequest;
import com.oleksa.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

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

    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDto user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        boolean isUpdated = userService.updateUser(username, user);
        return isUpdated
                ?
                new ResponseEntity<>(Collections.singletonMap("message", "User data is updated successfully"), HttpStatus.OK)
                :
                new ResponseEntity<>(Collections.singletonMap("message", "Failed to update user data"), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(
            @RequestBody PasswordUpdateRequest passwordRequest
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        boolean isUpdated = userService.updatePassword(username, passwordRequest.getOldPassword(), passwordRequest.getNewPassword());
        if (isUpdated) {
            return new ResponseEntity<>(Collections.singletonMap("message", "Password updated successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Collections.singletonMap("message", "Failed to update password"), HttpStatus.BAD_REQUEST);
        }
    }


}
