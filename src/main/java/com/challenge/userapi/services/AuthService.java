package com.challenge.userapi.services;

import com.challenge.userapi.dtos.AuthResponseDto;
import com.challenge.userapi.dtos.LoginRequestDto;
import com.challenge.userapi.entities.UserEntity;
import com.challenge.userapi.repositories.UserRepository;
import com.challenge.userapi.config.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    public AuthResponseDto login(LoginRequestDto loginDto) {
        UserEntity user = userRepository.findByUsername(loginDto.username())
                .or(() -> userRepository.findByEmail(loginDto.username()))
                .orElseThrow(() -> new RuntimeException("Identifiants invalides"));

        if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            throw new RuntimeException("Identifiants invalides");
        }

        String token = jwtUtils.generateToken(user.getUsername(), user.getEmail(), user.getRole().name());

        return new AuthResponseDto(token);
    }
}