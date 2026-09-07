package com.challenge.userapi.dtos;

public record JwtResponseDto(
        String token,
        String email
) {}
