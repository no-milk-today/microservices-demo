package ru.practicum.order.application.config;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "user-client")
@RequiredArgsConstructor
@Getter
public class UserClientProperties {
    private final String baseUrl;
}
