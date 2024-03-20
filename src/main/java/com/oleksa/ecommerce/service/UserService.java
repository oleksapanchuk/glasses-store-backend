package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.UserDto;
import com.oleksa.ecommerce.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService {

    Optional<UserDto> getUserById(Long id);

    boolean updateUser(String username, UserDto user);

    boolean updatePassword(String username, String oldPassword, String newPassword);

    User save(User user);

    User create(User user);

    User getByUsername(String username);

    UserDetailsService userDetailsService();

    User getCurrentUser();

    void getAdmin();

    boolean confirmUserAccount(String token);
}
