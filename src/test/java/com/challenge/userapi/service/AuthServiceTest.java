package com.challenge.userapi.service;

import com.challenge.userapi.dtos.AuthResponseDto;
import com.challenge.userapi.dtos.LoginRequestDto;
import com.challenge.userapi.enums.UserRoleEnum;
import com.challenge.userapi.entities.UserEntity;
import com.challenge.userapi.repositories.UserRepository;
import com.challenge.userapi.config.JwtUtils;
import com.challenge.userapi.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setUsername("johndoe");
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPassword("hashedPassword123");
        mockUser.setRole(UserRoleEnum.ROLE);
    }

    @Test
    @DisplayName("Login réussi via Username -> retourne le token JWT")
    void login_WithValidUsername_ShouldReturnToken() {
        // Given
        LoginRequestDto request = new LoginRequestDto("johndoe", "rawPassword123");

        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("rawPassword123", "hashedPassword123")).thenReturn(true);
        when(jwtUtils.generateToken("johndoe", "john.doe@example.com", "ROLE")).thenReturn("mocked.jwt.token");

        // When
        AuthResponseDto response = authService.login(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("mocked.jwt.token");

        verify(userRepository, times(1)).findByUsername("johndoe");
        verify(passwordEncoder, times(1)).matches("rawPassword123", "hashedPassword123");
        verify(jwtUtils, times(1)).generateToken("johndoe", "john.doe@example.com", "ROLE");
    }

    @Test
    @DisplayName("Login réussi via Email -> fallback réussi et retourne le token JWT")
    void login_WithValidEmail_ShouldReturnToken() {
        // Given
        LoginRequestDto request = new LoginRequestDto("john.doe@example.com", "rawPassword123");

        when(userRepository.findByUsername("john.doe@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("rawPassword123", "hashedPassword123")).thenReturn(true);
        when(jwtUtils.generateToken("johndoe", "john.doe@example.com", "ROLE")).thenReturn("mocked.jwt.token");

        // When
        AuthResponseDto response = authService.login(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("mocked.jwt.token");

        verify(userRepository, times(1)).findByUsername("john.doe@example.com");
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("Login échoué -> Utilisateur inexistant")
    void login_WithUserNotFound_ShouldThrowException() {
        // Given
        LoginRequestDto request = new LoginRequestDto("unknown_user", "password");

        when(userRepository.findByUsername("unknown_user")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("unknown_user")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Identifiants invalides");

        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtils, never()).generateToken(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Login échoué -> Mot de passe incorrect")
    void login_WithInvalidPassword_ShouldThrowException() {
        // Given
        LoginRequestDto request = new LoginRequestDto("johndoe", "wrongPassword");

        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword123")).thenReturn(false);

        // When / Then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Identifiants invalides");

        verify(jwtUtils, never()).generateToken(anyString(), anyString(), anyString());
    }
}