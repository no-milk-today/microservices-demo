package ru.practicum.user.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.application.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
