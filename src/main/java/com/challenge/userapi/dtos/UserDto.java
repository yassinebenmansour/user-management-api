package com.challenge.userapi.dtos;

import com.challenge.userapi.enums.UserRoleEnum;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserDto(
        String firstName,
        String lastName,
        LocalDate birthDate,
        String city,
        String country,
        String avatar,
        String company,
        String jobPosition,
        String mobile,
        String username,
        String email,
        String password,
        UserRoleEnum role
) {
}
