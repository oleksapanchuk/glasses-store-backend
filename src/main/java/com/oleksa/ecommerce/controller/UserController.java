package com.oleksa.ecommerce.controller;

import com.oleksa.ecommerce.constants.AppConstants;
import com.oleksa.ecommerce.dto.ResponseDto;
import com.oleksa.ecommerce.dto.UserDto;
import com.oleksa.ecommerce.dto.request.PasswordUpdateRequest;
import com.oleksa.ecommerce.service.EmailService;
import com.oleksa.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/{user-id}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable(name = "user-id") Long userId
    ) {

        UserDto user = userService.fetchUserById(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<UserDto> getByUsername(
            @PathVariable String email
    ) {

        UserDto user = userService.fetchUserByUsername(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDto user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        boolean isUpdated = userService.updateUser(email, user);
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
        String email = auth.getName();

        boolean isUpdated = userService.updatePassword(email, passwordRequest.getOldPassword(), passwordRequest.getNewPassword());
        if (isUpdated) {
            return new ResponseEntity<>(Collections.singletonMap("message", "Password updated successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Collections.singletonMap("message", "Failed to update password"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/send-email-confirmation")
    public ResponseEntity<ResponseDto> sendEmailConfirmation(
            @RequestParam String email
    ) {
        boolean isSent = emailService.sendConfirmationEmail(email);

        if (isSent) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AppConstants.STATUS_200, AppConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AppConstants.STATUS_417, AppConstants.MESSAGE_417_DELETE));
        }
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<ResponseDto> confirmAccount(@RequestParam String token) {

        boolean isVerified = userService.confirmUserAccount(token);

        if (isVerified) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AppConstants.STATUS_200, AppConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AppConstants.STATUS_417, AppConstants.MESSAGE_417_DELETE));
        }
    }

}
