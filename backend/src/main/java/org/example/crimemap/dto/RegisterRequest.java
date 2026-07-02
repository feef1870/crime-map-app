package org.example.crimemap.dto;

public record RegisterRequest(
        String email,
        String password
) {
}
