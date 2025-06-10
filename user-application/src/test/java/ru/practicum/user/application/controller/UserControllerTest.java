package ru.practicum.user.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.user.application.dto.UserRequestDto;
import ru.practicum.user.application.dto.UserResponseDto;
import ru.practicum.user.application.service.UserService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    private static final String BASE_URL = "/users";
    private static final String REGISTER_URL = BASE_URL + "/register";

    private static final String NAME = "Alice";
    private static final String EMAIL = "alice@yandex.ru";
    private static final String PASSWORD = "password123";
    private static final List<String> ROLES = List.of("USER");
    private static final long USER_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestDto validRequest;
    private UserResponseDto validResponse;

    @BeforeEach
    void setUp() {
        validRequest = new UserRequestDto(NAME, EMAIL, PASSWORD, ROLES);
        validResponse = new UserResponseDto(USER_ID, NAME, EMAIL);
    }

    @Test
    @DisplayName("POST /users/register — должен вернуть 201 и созданного пользователя")
    void shouldCreateUser() throws Exception {
        when(userService.createUser(any(UserRequestDto.class))).thenReturn(validResponse);

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    @DisplayName("POST /users/register — должен вернуть 400 при неверных данных")
    void shouldReturn400WhenInvalid() throws Exception {
        var invalidRequest = new UserRequestDto("", "not-an-email", "", Collections.emptyList());

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.name").value("Name must not be blank"))
                .andExpect(jsonPath("$.errors.email").value("Email must be valid"))
                .andExpect(jsonPath("$.errors.password").value("Password must not be blank"));
    }

    @Test
    @DisplayName("GET /users/{id} — должен вернуть пользователя")
    void shouldGetUserById() throws Exception {
        when(userService.getUser(USER_ID)).thenReturn(validResponse);

        mockMvc.perform(get(BASE_URL + "/" + USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    @DisplayName("GET /users/{id} — должен вернуть 404 если не найден")
    void shouldReturn404WhenUserNotFound() throws Exception {
        long missingId = 999L;
        when(userService.getUser(missingId))
                .thenThrow(new IllegalArgumentException("User with ID " + missingId + " not found"));

        mockMvc.perform(get(BASE_URL + "/" + missingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with ID 999 not found"));
    }

    private String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
}
