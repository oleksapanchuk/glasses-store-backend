package com.oleksa.ecommerce.mapper;

import com.oleksa.ecommerce.dto.UserDto;
import com.oleksa.ecommerce.entity.User;

public class UsersMapper {

    public static UserDto mapToUsersDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .imageUrl(user.getImageUrl())
                .build();
    }

    public static User mapToUsers(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .username(userDto.getUsername())
//                .role(userDto.getRole())
                .imageUrl(userDto.getImageUrl())
                .build();
    }


}
