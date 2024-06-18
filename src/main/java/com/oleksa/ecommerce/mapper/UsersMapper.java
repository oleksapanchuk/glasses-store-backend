package com.oleksa.ecommerce.mapper;

import com.oleksa.ecommerce.dto.UserDto;
import com.oleksa.ecommerce.entity.Role;
import com.oleksa.ecommerce.entity.User;

public class UsersMapper {

    public static UserDto mapToUsersDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .subscribed(user.isSubscribed())
                .verified(user.isVerified())
                .role(user.getRole().name())
                .build();
    }

    public static User mapToUser(UserDto userDto, User user) {
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setSubscribed(userDto.isSubscribed());
        user.setVerified(userDto.isVerified());
        user.setRole(Role.valueOf(userDto.getRole()));
        return user;
    }


}
