package ru.practicum.order.application.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import ru.practicum.order.application.config.UserClientProperties;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserClientTest {

    private static final String BASE_URL = "http://mock";
    private static final long USER_ID = 1L;
    private static final long NOT_FOUND_ID = 999L;

    @Test
    @DisplayName("userExists(id) — должен вернуть true, если ответ 200 OK")
    void shouldReturnTrueWhenResponseIs200() {
        ClientResponse response = ClientResponse.create(HttpStatus.OK).build();

        ExchangeFunction exchangeFunction = mock(ExchangeFunction.class);
        when(exchangeFunction.exchange(any(ClientRequest.class)))
                .thenReturn(Mono.just(response));

        WebClient.Builder builder = WebClient.builder().exchangeFunction(exchangeFunction);
        UserClient client = new UserClient(builder, new UserClientProperties(BASE_URL));

        boolean result = client.userExists(USER_ID);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("userExists(id) — должен вернуть false, если ответ 404 Not Found")
    void shouldReturnFalseWhenResponseIs404() {
        WebClientResponseException notFound = WebClientResponseException.create(
                404, "Not Found", null, null, StandardCharsets.UTF_8, null
        );

        ExchangeFunction exchangeFunction = mock(ExchangeFunction.class);
        when(exchangeFunction.exchange(any(ClientRequest.class)))
                .thenReturn(Mono.error(notFound));

        WebClient.Builder builder = WebClient.builder().exchangeFunction(exchangeFunction);
        UserClient client = new UserClient(builder, new UserClientProperties(BASE_URL));

        boolean result = client.userExists(NOT_FOUND_ID);

        assertThat(result).isFalse();
    }
}
