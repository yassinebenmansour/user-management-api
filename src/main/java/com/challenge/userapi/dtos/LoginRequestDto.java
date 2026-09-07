package com.challenge.userapi.dtos;

public record LoginRequestDto(
        String username,
        String password
) {}