package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.UserDto;
import com.oleksa.ecommerce.entity.Role;
import com.oleksa.ecommerce.entity.User;
import com.oleksa.ecommerce.exception.ResourceNotFoundException;
import com.oleksa.ecommerce.exception.UserAlreadyExistException;
import com.oleksa.ecommerce.mapper.UsersMapper;
import com.oleksa.ecommerce.repository.UserRepository;
import com.oleksa.ecommerce.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .username("testUsername")
                .email("test@gmail.com")
                .role(Role.ROLE_USER)
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .username("testUsername")
                .email("test@gmail.com")
                .role("ROLE_USER")
                .build();
    }

    @Test
    void shouldCreateUserSuccessfully() {
        User user = new User();
        user.setUsername("testUsername");
        user.setEmail("testEmail");

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        userService.createUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExistsOnCreateUser() {
        User user = new User();
        user.setUsername("testUsername");

        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> userService.createUser(user));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExistsOnCreateUser() {
        User user = new User();
        user.setEmail("testEmail");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> userService.createUser(user));
    }

    @Test
    void shouldGetUserByIdSuccessfully() {
        when(userRepository.findById(any())).thenReturn(Optional.of(UsersMapper.mapToUser(userDto, new User())));

        UserDto result = userService.fetchUserById(1L);

        assertNotNull(result);
        assertEquals(userDto, result);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnGetUserById() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.fetchUserById(1L));
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        userDto.setFirstName("testFirstName");
        userDto.setLastName("testLastName");
        userDto.setPhoneNumber("testPhoneNumber");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenReturn(new User());

        boolean result = userService.updateUser("testUsername", userDto);

        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnUpdateUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.updateUser("testUsername", userDto));
    }

    @Test
    void shouldUpdatePasswordSuccessfully() {
        user.setPassword("oldPassword");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        boolean result = userService.updatePassword("testUsername", "oldPassword", "newPassword");

        assertTrue(result);
    }

    @Test
    void shouldNotUpdatePasswordWhenOldPasswordIsIncorrect() {
        user.setPassword("oldPassword");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        boolean result = userService.updatePassword("testUsername", "oldPassword", "newPassword");

        assertFalse(result);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnUpdatePassword() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.updatePassword("testUsername", "oldPassword", "newPassword"));
    }

    @Test
    void shouldGetByUsernameSuccessfully() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        UserDto result = userService.fetchUserByUsername("testUsername");

        assertNotNull(result);
        assertEquals("testUsername", result.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnGetByUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.fetchUserByUsername("testUsername")
        );
    }

    @Test
    void shouldConfirmUserAccountSuccessfully() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(jwtService.extractEmail(anyString())).thenReturn("testUsername");

        boolean result = userService.confirmUserAccount("token");

        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnConfirmUserAccount() {
        when(jwtService.extractEmail(anyString())).thenReturn("testUsername");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.confirmUserAccount("token"));
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        User user = new User();
        user.setUsername("testUsername");

        when(userRepository.findByUsername("testUsername")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("testUsername");

        assertNotNull(result);
        assertEquals("testUsername", result.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnLoadUserByUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("testUsername"));
    }

    @Test
    void shouldReturnUserDetailsServiceSuccessfully() {
        UserDetailsService result = userService.userDetailsService();

        assertNotNull(result);
    }

}
