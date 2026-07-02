package org.example.crimemap.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.crimemap.dto.IncidentRequest;
import org.example.crimemap.dto.IncidentResponse;
import org.example.crimemap.entities.User;
import org.example.crimemap.security.SecurityUser;
import org.example.crimemap.services.IncidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@AllArgsConstructor
public class IncidentController {
    private final IncidentService incidentService;

    @PostMapping
    public ResponseEntity<IncidentResponse> createIncident(
            @Valid @RequestBody IncidentRequest request,
            @AuthenticationPrincipal SecurityUser securityUser) {

        User user = securityUser.getUser();

        IncidentResponse response = incidentService.createIncident(request, user);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<IncidentResponse>> getActiveIncidents() {
        List<IncidentResponse> activePins = incidentService.getActiveIncidents();

        return ResponseEntity.ok(activePins);
    }
}
