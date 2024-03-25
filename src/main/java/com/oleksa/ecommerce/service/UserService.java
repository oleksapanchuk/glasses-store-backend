package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.UserDto;
import com.oleksa.ecommerce.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    void createUser(User user);

    UserDto fetchUserById(Long id);

    UserDto fetchUserByUsername(String username);

    boolean updateUser(String username, UserDto user);

    boolean updatePassword(String username, String oldPassword, String newPassword);

    boolean confirmUserAccount(String token);

    UserDetailsService userDetailsService();
}
