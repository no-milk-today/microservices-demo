package ru.practicum.user.application.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Set;

@Component
public class JwtUtil {
    @Value("${security.jwt.key}")
    private String internalSecret;

    @Value("${security.jwt.expiration_time_minutes}")
    private long internalExpirationTime;

    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(username)
                .expiration(Date.from(
                                LocalDateTime.now().plusMinutes(
                                        internalExpirationTime).toInstant(ZoneOffset.UTC)
                        )
                )
                .claim("roles", String.join(",", roles))
                .signWith(Keys.hmacShaKeyFor(internalSecret.getBytes()))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(internalSecret.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(internalSecret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}

