package ru.practicum.user.application.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.user.application.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    private static final String NAME = "Alice";
    private static final String EMAIL = "alice@yandex.ru";

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("save() — должен сохранить пользователя")
    void shouldSaveUser() {
        User saved = repository.save(newUser(NAME, EMAIL));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(NAME);
        assertThat(saved.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    @DisplayName("findById() — должен вернуть пользователя, если найден")
    void shouldFindUserById() {
        User saved = repository.save(newUser("Ivan", "ivan@yandex.ru"));

        Optional<User> result = repository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Ivan");
        assertThat(result.get().getEmail()).isEqualTo("ivan@yandex.ru");
    }

    @Test
    @DisplayName("findById() — должен вернуть пусто, если пользователя нет")
    void shouldReturnEmptyIfUserNotFound() {
        Optional<User> result = repository.findById(999L);

        assertThat(result).isEmpty();
    }

    private User newUser(String name, String email) {
        return User.builder()
                .name(name)
                .email(email)
                .build();
    }
}
