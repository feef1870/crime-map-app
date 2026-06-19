package org.example.crimemap.dto;

public record UserDto(
        Long id,
        String email,
        double trustScore
) {
}
