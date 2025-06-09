package ru.practicum.user.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.practicum.user.application.dto.UserRequestDto;
import ru.practicum.user.application.dto.UserResponseDto;
import ru.practicum.user.application.mapper.UserMapper;
import ru.practicum.user.application.model.User;
import ru.practicum.user.application.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final static Long USER_ID = 1L;
    private final static String USER_NAME = "Alice";
    private final static String USER_EMAIL = "alice@yandex.ru";
    private static final String USER_PASSWORD = "password123";

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService underTest;

    @Test
    @DisplayName("createUser() — должен сохранить и вернуть пользователя")
    void shouldCreateUser() {
        var request = UserRequestDto.builder()
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();

        var user = User.builder()
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD) // Пароль не хэшируем в тестах
                .build();

        var saved = User.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();

        var responseDto = UserResponseDto.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();

        when(mapper.toEntity(request)).thenReturn(user);
        when(repository.save(user)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(responseDto);

        var result = underTest.createUser(request);

        assertThat(result.getId()).isEqualTo(USER_ID);
        assertThat(result.getName()).isEqualTo(USER_NAME);
        assertThat(result.getEmail()).isEqualTo(USER_EMAIL);
    }

    @Test
    @DisplayName("getUser(id) — должен вернуть пользователя, если он найден")
    void shouldReturnUserById() {
        var user = User.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD) // Пароль не хэшируем в тестах
                .build();

        var dto = UserResponseDto.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();

        when(repository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(mapper.toDto(user)).thenReturn(dto);

        UserResponseDto result = underTest.getUser(USER_ID);

        assertThat(result.getId()).isEqualTo(USER_ID);
        assertThat(result.getName()).isEqualTo(USER_NAME);
        assertThat(result.getEmail()).isEqualTo(USER_EMAIL);
    }

    @Test
    @DisplayName("getUser(id) — должен выбросить исключение, если пользователь не найден")
    void shouldThrowWhenUserNotFound() {
        Long wrongId = 42L;

        when(repository.findById(wrongId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getUser(wrongId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with ID 42 not found");
    }
}
