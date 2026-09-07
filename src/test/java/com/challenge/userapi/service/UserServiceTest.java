package com.challenge.userapi.service;

import com.challenge.userapi.dtos.UserDto;
import com.challenge.userapi.enums.UserRoleEnum;
import com.challenge.userapi.entities.UserEntity;
import com.challenge.userapi.mappers.UserMapper;
import com.challenge.userapi.repositories.UserRepository;
import com.challenge.userapi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserEntity mockUser;
    private UserDto mockDto;

    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setUsername("johndoe");
        mockUser.setEmail("john.doe@example.com");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setRole(UserRoleEnum.ROLE);

        mockDto = new UserDto(
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "Paris",
                "France",
                "avatar.png",
                "TechCorp",
                "Developer",
                "+33612345678",
                "johndoe",
                "john.doe@example.com",
                "********",
                UserRoleEnum.ROLE
        );
    }

    @Test
    @DisplayName("getUserByUsername -> Retourne le DTO si l'utilisateur existe")
    void getUserByUsername_WhenUserExists_ShouldReturnUserDto() {
        // Given
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockUser));
        when(userMapper.toDto(mockUser)).thenReturn(mockDto);

        // When
        UserDto result = userService.getUserByUsername("johndoe");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("johndoe");
        assertThat(result.email()).isEqualTo("john.doe@example.com");

        verify(userRepository, times(1)).findByUsername("johndoe");
        verify(userMapper, times(1)).toDto(mockUser);
    }

    @Test
    @DisplayName("getUserByUsername -> Lève une exception si l'utilisateur n'existe pas")
    void getUserByUsername_WhenUserDoesNotExist_ShouldThrowException() {
        // Given
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> userService.getUserByUsername("unknown"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Utilisateur non trouvé avec le username : unknown");

        verify(userMapper, never()).toDto(any());
    }
}