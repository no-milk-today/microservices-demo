package ru.practicum.order.application.client;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.practicum.order.application.config.UserClientProperties;

@Slf4j
@Component
public class UserClient {

    private final WebClient webClient;

    public UserClient(WebClient.Builder builder, UserClientProperties properties) {
        this.webClient = builder
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    public boolean userExists(Long id) {
        try {
            webClient.get()
                    .uri("/users/{id}", id)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("User {} exists", id);
            return true;
        } catch (WebClientResponseException.NotFound e) {
            log.warn("User {} not found", id);
            return false;
        } catch (Exception error) {
            log.error("Error checking user {}", id, error);
            return false;
        }
    }
}
