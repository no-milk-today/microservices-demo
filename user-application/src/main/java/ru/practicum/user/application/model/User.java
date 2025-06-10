package ru.practicum.user.application.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email must not be blank")
    private String email;

    @Setter
    @NotBlank(message = "Password must not be blank")
    private String password;

    // Коллекция ролей: не null и не пустая
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    @NotNull(message = "Roles must not be null")
    @NotEmpty(message = "Roles must not be empty")
    private Set<String> roles = new HashSet<>();

    // Lombok @Getter не нужен для этого метода, чтобы не было конфликта
    public List<String> getRoles() {
        return new ArrayList<>(roles);
    }
}
