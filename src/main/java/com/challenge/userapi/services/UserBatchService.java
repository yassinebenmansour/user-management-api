package com.challenge.userapi.services;

import com.challenge.userapi.dtos.BatchResponseDto;
import com.challenge.userapi.dtos.UserDto;
import com.challenge.userapi.entities.UserEntity;
import com.challenge.userapi.mappers.UserMapper;
import com.challenge.userapi.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Service
public class UserBatchService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public UserBatchService(UserRepository userRepository, UserMapper userMapper,
                            PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    public BatchResponseDto processBatchUpload(MultipartFile file) throws IOException {

        List<UserDto> users = objectMapper.readValue(
                file.getInputStream(),
                new TypeReference<List<UserDto>>() {}
        );

        int total = users.size();
        int success = 0;
        int failed = 0;

        for (UserDto dto : users) {
            boolean usernameExists = userRepository.existsByUsername(dto.username());
            boolean emailExists = userRepository.existsByEmail(dto.email());

            if (usernameExists || emailExists) {
                failed++;
            } else {
                UserEntity entity = userMapper.toEntity(dto);

                entity.setPassword(passwordEncoder.encode(dto.password()));
                userRepository.save(entity);
                success++;
            }
        }

        return new BatchResponseDto(total, success, failed);
    }
}