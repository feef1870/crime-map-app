package org.example.crimemap.dto;

import org.example.crimemap.enums.IncidentCategory;

import java.time.LocalDateTime;

public record IncidentResponse(
        Long id,
        Double latitude,
        Double longitude,
        IncidentCategory category,
        Integer severityScore,
        LocalDateTime incidentTime
) {
}
