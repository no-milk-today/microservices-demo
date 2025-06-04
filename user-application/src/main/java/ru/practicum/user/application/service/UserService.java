package ru.practicum.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.user.application.dto.UserRequestDto;
import ru.practicum.user.application.dto.UserResponseDto;
import ru.practicum.user.application.mapper.UserMapper;
import ru.practicum.user.application.model.User;
import ru.practicum.user.application.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserResponseDto createUser(UserRequestDto dto) {
        User user = mapper.toEntity(dto);
        User saved = repository.save(user);
        return mapper.toDto(saved);
    }

    public UserResponseDto getUser(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found"));
    }
}
