package ru.practicum.user.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    @Test
    @DisplayName("createUser() — должен сохранить и вернуть пользователя")
    void shouldCreateUser() {
        UserRequestDto request = UserRequestDto.builder()
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();

        User user = User.builder()
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();

        User saved = User.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();

        when(mapper.toEntity(request)).thenReturn(user);
        when(repository.save(user)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(responseDto);

        UserResponseDto result = service.createUser(request);

        assertThat(result.getId()).isEqualTo(USER_ID);
        assertThat(result.getName()).isEqualTo(USER_NAME);
        assertThat(result.getEmail()).isEqualTo(USER_EMAIL);
    }

    @Test
    @DisplayName("getUser(id) — должен вернуть пользователя, если он найден")
    void shouldReturnUserById() {
        User user = User.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();

        UserResponseDto dto = UserResponseDto.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();

        when(repository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(mapper.toDto(user)).thenReturn(dto);

        UserResponseDto result = service.getUser(USER_ID);

        assertThat(result.getId()).isEqualTo(USER_ID);
        assertThat(result.getName()).isEqualTo(USER_NAME);
        assertThat(result.getEmail()).isEqualTo(USER_EMAIL);
    }

    @Test
    @DisplayName("getUser(id) — должен выбросить исключение, если пользователь не найден")
    void shouldThrowWhenUserNotFound() {
        Long wrongId = 42L;

        when(repository.findById(wrongId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getUser(wrongId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with ID 42 not found");
    }
}
