package org.example.crimemap.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.crimemap.dto.IncidentRequest;
import org.example.crimemap.dto.IncidentResponse;
import org.example.crimemap.entities.User;
import org.example.crimemap.security.SecurityUser;
import org.example.crimemap.services.IncidentService;
import org.example.crimemap.services.SseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@AllArgsConstructor
public class IncidentController {
    private final IncidentService incidentService;
    private final SseService sseService;

    @GetMapping("/stream")
    public SseEmitter streamIncidents() {
        return sseService.subscribe();
    }

    @PostMapping
    public ResponseEntity<IncidentResponse> createIncident(
            @Valid @RequestBody IncidentRequest request,
            @AuthenticationPrincipal SecurityUser securityUser) {

        User user = securityUser.getUser();

        IncidentResponse response = incidentService.createIncident(request, user);

        sseService.broadcast(response);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<IncidentResponse>> getActiveIncidents() {
        List<IncidentResponse> activePins = incidentService.getActiveIncidents();

        return ResponseEntity.ok(activePins);
    }
}
