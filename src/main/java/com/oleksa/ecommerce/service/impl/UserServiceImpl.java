package com.oleksa.ecommerce.service.impl;

import com.oleksa.ecommerce.dto.UserDto;
import com.oleksa.ecommerce.entity.Role;
import com.oleksa.ecommerce.entity.User;
import com.oleksa.ecommerce.exception.ResourceNotFoundException;
import com.oleksa.ecommerce.exception.UserAlreadyExistException;
import com.oleksa.ecommerce.mapper.UsersMapper;
import com.oleksa.ecommerce.repository.UserRepository;
import com.oleksa.ecommerce.service.JwtService;
import com.oleksa.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Getting user by ID
     *
     * @param id The ID of the user
     * @return UserDTO
     */
    @Override
    public Optional<UserDto> getUserById(Long id) {
        return Optional.of(
                repository.findById(id)
                        .map(UsersMapper::mapToUsersDto)
                        .orElseThrow(() -> new UsernameNotFoundException("User is not found"))
        );
    }

    @Override
    public boolean updateUser(String username, UserDto userDto) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());

        repository.save(user);

        return true;
    }

    @Override
    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            repository.save(user);
            return true;
        }
        return false;
    }

    /**
     * Saving a user
     *
     * @return saved user
     */
    public User save(User user) {
        return repository.save(user);
    }

    /**
     * Creating a user
     *
     * @return created user
     */
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistException("User with the same username already exists.");
        }
        if (repository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("User with the same email already exists.");
        }

        return save(user);
    }

    /**
     * Getting user by username
     *
     * @return user
     */
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found"));
    }

    /**
     * Getting user by username
     * <p>
     * Needed for Spring Security
     *
     * @return username
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Getting the current user
     *
     * @return current user
     */
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }


    /**
     * Выдача прав администратора текущему пользователю
     * <p>
     * Нужен для демонстрации
     */
    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));

    }

    public boolean confirmUserAccount(String token) {

        String username = jwtService.extractUserName(token);
        log.info("Username from token: {}", username);

        User user = repository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username)
        );

        user.setVerified(true);
        repository.save(user);
        return true;
    }
}
