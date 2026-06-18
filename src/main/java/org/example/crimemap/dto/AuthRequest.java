package org.example.crimemap.dto;

public record AuthRequest(
        String email,
        String password
) {
}
