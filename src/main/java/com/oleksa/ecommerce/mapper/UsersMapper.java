package com.oleksa.ecommerce.mapper;

import com.oleksa.ecommerce.dto.UserDto;
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

    public static User mapToUsers(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .phoneNumber(userDto.getPhoneNumber())
                .subscribed(userDto.isSubscribed())
                .verified(userDto.isVerified())
//                .role(userDto.getRole())
                .build();
    }


}
