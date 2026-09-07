package com.challenge.userapi.services;

import com.challenge.userapi.dtos.UserDto;
import com.challenge.userapi.entities.UserEntity;
import com.challenge.userapi.mappers.UserMapper;
import com.challenge.userapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto getUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec le username : " + username));

        return userMapper.toDto(user);
    }
}