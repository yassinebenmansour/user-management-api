package com.challenge.userapi.dtos;

public record BatchResponseDto(
        int totalRecords,
        int successCount,
        int failedCount
) {}