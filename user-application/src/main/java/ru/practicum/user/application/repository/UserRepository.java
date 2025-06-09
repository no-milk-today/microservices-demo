package ru.practicum.user.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.application.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
