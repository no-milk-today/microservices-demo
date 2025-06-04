package ru.practicum.order.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.order.application.dto.OrderRequestDto;
import ru.practicum.order.application.dto.OrderResponseDto;
import ru.practicum.order.application.service.OrderService;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    private static final long ORDER_ID = 1L;
    private static final long USER_ID = 1L;
    private static final String PRODUCT = "Book";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderRequestDto requestDto;
    private OrderResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = OrderRequestDto.builder()
                .userId(USER_ID)
                .product(PRODUCT)
                .build();

        responseDto = OrderResponseDto.builder()
                .id(ORDER_ID)
                .userId(USER_ID)
                .product(PRODUCT)
                .build();
    }

    @Test
    @DisplayName("POST /orders — должен вернуть 201 и тело заказа")
    void shouldReturn201WhenOrderCreated() throws Exception {
        when(orderService.createOrder(any(OrderRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ORDER_ID))
                .andExpect(jsonPath("$.product").value(PRODUCT))
                .andExpect(jsonPath("$.userId").value(USER_ID));
    }

    @Test
    @DisplayName("POST /orders — должен вернуть 404, если пользователь не найден")
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(orderService.createOrder(any(OrderRequestDto.class)))
                .thenThrow(new IllegalArgumentException("User with ID 999 does not exist"));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(OrderRequestDto.builder()
                                .userId(999L)
                                .product("Game")
                                .build())))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with ID 999 does not exist"));
    }

    private String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
}
