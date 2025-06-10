package ru.practicum.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.practicum.user.application.dto.UserRequestDto;
import ru.practicum.user.application.dto.UserResponseDto;
import ru.practicum.user.application.mapper.UserMapper;
import ru.practicum.user.application.model.User;
import ru.practicum.user.application.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto createUser(UserRequestDto dto) {
        // конвертация DTO → Entity
        User user = mapper.toEntity(dto);
        // хэшируем пароль
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        log.debug("Attempting to create user with email: {}", dto.getEmail());
        User saved = repository.save(user);
        log.info("User created with ID: {}", saved.getId());
        return mapper.toDto(saved);
    }

    public UserResponseDto getUser(Long id) {
        log.debug("Fetching user with ID: {}", id);
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);
        User user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));
        List<String> roles = user.getRoles();
        List<GrantedAuthority> authorities =
                roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

        log.debug("Loaded user with email: {} and authorities: {}", user.getEmail(), authorities);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}