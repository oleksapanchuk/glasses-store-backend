package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.UserDto;
import com.oleksa.ecommerce.entity.Role;
import com.oleksa.ecommerce.entity.User;
import com.oleksa.ecommerce.exception.ResourceNotFoundException;
import com.oleksa.ecommerce.exception.UserAlreadyExistException;
import com.oleksa.ecommerce.repository.UserRepository;
import com.oleksa.ecommerce.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository repository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldCreateUserSuccessfully_WhenUserDoesNotExist() {
        User user = new User();
        user.setEmail("testEmail");

        when(repository.existsByEmail(anyString())).thenReturn(false);

        userService.createUser(user);

        verify(repository, times(1)).save(user);
    }


    @Test
    void shouldThrowUserAlreadyExistException_WhenEmailExists() {
        User user = new User();
        user.setEmail("existingEmail");

        when(repository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> userService.createUser(user));
    }

    @Test
    void shouldFetchUserByIdSuccessfully_WhenUserExists() {
        Long userId = 1L;
        User user = new User();
        user.setRole(Role.ROLE_USER);
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        UserDto result = userService.fetchUserById(userId);

        assertNotNull(result);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
        Long userId = 1L;
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.fetchUserById(userId));
    }

    @Test
    void shouldUpdateUserSuccessfully_WhenUserExists() {
        String email = "testEmail";
        UserDto userDto = UserDto.builder().build();
        User user = new User();
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        boolean result = userService.updateUser(email, userDto);

        assertTrue(result);
    }

    @Test
    void shouldThrowUsernameNotFoundException_WhenUserDoesNotExistOnUpdate() {
        String email = "nonExistingEmail";
        UserDto userDto = UserDto.builder().build();

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.updateUser(email, userDto));
    }

    @Test
    void shouldUpdatePasswordSuccessfully_WhenOldPasswordIsCorrect() {
        String email = "testEmail";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        User user = new User();
        user.setPassword(oldPassword);
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);

        boolean result = userService.updatePassword(email, oldPassword, newPassword);

        assertTrue(result);
    }

    @Test
    void shouldNotUpdatePassword_WhenOldPasswordIsIncorrect() {
        String email = "testEmail";
        String oldPassword = "wrongOldPassword";
        String newPassword = "newPassword";
        User user = new User();
        user.setPassword("oldPassword");
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(false);

        boolean result = userService.updatePassword(email, oldPassword, newPassword);

        assertFalse(result);
    }

    @Test
    void shouldThrowUsernameNotFoundException_WhenUserDoesNotExistOnUpdatePassword() {
        String email = "nonExistingEmail";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.updatePassword(email, oldPassword, newPassword));
    }

    @Test
    void shouldFetchUserByUsernameSuccessfully_WhenUserExists() {
        String email = "testEmail";
        User user = new User();
        user.setRole(Role.ROLE_USER);
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDto result = userService.fetchUserByUsername(email);

        assertNotNull(result);
    }

    @Test
    void shouldThrowUsernameNotFoundException_WhenUserDoesNotExistOnFetchByUsername() {
        String email = "nonExistingEmail";

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.fetchUserByUsername(email));
    }

    @Test
    void shouldConfirmUserAccountSuccessfully_WhenUserExists() {
        String token = "token";
        String email = "testEmail";
        User user = new User();
        when(jwtService.extractEmail(token)).thenReturn(email);
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        boolean result = userService.confirmUserAccount(token);

        assertTrue(result);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenUserDoesNotExistOnConfirmAccount() {
        String token = "token";
        String email = "nonExistingEmail";

        when(jwtService.extractEmail(token)).thenReturn(email);
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.confirmUserAccount(token));
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully_WhenUserExists() {
        String email = "testEmail";
        User user = new User();
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername(email);

        assertNotNull(result);
    }

    @Test
    void shouldThrowUsernameNotFoundException_WhenUserDoesNotExistOnLoadByUsername() {
        String email = "nonExistingEmail";

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
    }

}
