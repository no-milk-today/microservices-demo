package ru.practicum.user.application.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.application.dto.UserRequestDto;
import ru.practicum.user.application.dto.UserResponseDto;
import ru.practicum.user.application.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@RequestBody @Valid UserRequestDto dto) {
        return userService.createUser(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public UserResponseDto get(@PathVariable Long id) {
        return userService.getUser(id);
    }
}
