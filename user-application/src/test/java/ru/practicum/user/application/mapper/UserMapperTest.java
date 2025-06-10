package ru.practicum.user.application.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.user.application.dto.UserRequestDto;
import ru.practicum.user.application.dto.UserResponseDto;
import ru.practicum.user.application.model.User;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private static final Long USER_ID = 1L;
    private static final String NAME = "Alice";
    private static final String EMAIL = "alice@yandex.ru";
    private static final String PASSWORD = "password123";
    private static final List<String> ROLES_LIST = List.of("USER");

    private final UserMapper mapper = new UserMapperImpl();

    @Test
    @DisplayName("Должен маппить UserRequestDto в User без id")
    void shouldMapToEntity() {
        UserRequestDto dto = new UserRequestDto(NAME, EMAIL, PASSWORD, ROLES_LIST);

        User user = mapper.toEntity(dto);

        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    @DisplayName("Должен маппить User в UserResponseDto")
    void shouldMapToDto() {
        User user = User.builder()
                .id(USER_ID)
                .name(NAME)
                .email(EMAIL)
                .roles(Set.of("USER"))
                .build();

        UserResponseDto dto = mapper.toDto(user);

        assertThat(dto.getId()).isEqualTo(USER_ID);
        assertThat(dto.getName()).isEqualTo(NAME);
        assertThat(dto.getEmail()).isEqualTo(EMAIL);
    }
}
