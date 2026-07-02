package org.example.crimemap.dto;

public record AuthResponse(
        String token,
        Long id,
        String email

) {
}
