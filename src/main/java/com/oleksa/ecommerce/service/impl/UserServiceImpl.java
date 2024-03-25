package com.oleksa.ecommerce.service.impl;

import com.oleksa.ecommerce.dto.UserDto;
import com.oleksa.ecommerce.entity.User;
import com.oleksa.ecommerce.exception.ResourceNotFoundException;
import com.oleksa.ecommerce.exception.UserAlreadyExistException;
import com.oleksa.ecommerce.mapper.UsersMapper;
import com.oleksa.ecommerce.repository.UserRepository;
import com.oleksa.ecommerce.service.JwtService;
import com.oleksa.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistException("User with the same username already exists.");
        }
        if (repository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("User with the same email already exists.");
        }

        repository.save(user);
    }

    /**
     * Getting user by ID
     *
     * @param id The ID of the user
     * @return UserDTO
     */
    @Override
    public UserDto fetchUserById(Long id) {
        User user = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return UsersMapper.mapToUsersDto(user);
    }

    /**
     * Getting user by username
     *
     * @return user
     */
    @Override
    public UserDto fetchUserByUsername(String username) {
        User user = repository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User is not found")
        );

        return UsersMapper.mapToUsersDto(user);
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

    @Override
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

    /**
     * Getting user by username
     * <p>
     * Needed for Spring Security
     *
     * @return username
     */
    public UserDetailsService userDetailsService() {
        return this;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User with username " + username + " not found")
        );
    }

}
