package ru.practicum.user.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.user.application.dto.AuthRequest;
import ru.practicum.user.application.dto.AuthResponse;
import ru.practicum.user.application.security.JwtUtil;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        // Пытаемся аутентифицировать пользователя
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
            )
        );

        // If success auth, получаем UserDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // generateToken, передавая имя пользователя и его роли (пока просто USER)
        String token = jwtUtil.generateToken(
            userDetails.getUsername(),
            userDetails.getAuthorities()
                       .stream()
                       .map(GrantedAuthority::getAuthority)
                       .collect(Collectors.toSet())
        );

        return ResponseEntity.ok(new AuthResponse(token));
    }

}


