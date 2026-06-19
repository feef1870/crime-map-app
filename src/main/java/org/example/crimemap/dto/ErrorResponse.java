package org.example.crimemap.dto;

import java.time.Instant;

public record ErrorResponse(
        String errorCode,
        String message,
        int status,
        Instant timestamp,
        String path
) {
}
