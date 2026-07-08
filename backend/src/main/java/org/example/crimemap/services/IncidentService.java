package org.example.crimemap.services;

import lombok.AllArgsConstructor;
import org.example.crimemap.dto.IncidentRequest;
import org.example.crimemap.dto.IncidentResponse;
import org.example.crimemap.entities.Incident;
import org.example.crimemap.entities.User;
import org.example.crimemap.repositories.IncidentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final SseService sseService;

    public IncidentResponse createIncident(IncidentRequest request, User user) {
        Incident incident = new Incident();

        incident.setLatitude(request.latitude());
        incident.setLongitude(request.longitude());
        incident.setCategory(request.category());
        incident.setSeverityScore(request.severityScore());
        incident.setIncidentTime(request.incidentTime());
        incident.setUser(user);

        Incident savedIncident = incidentRepository.save(incident);

        IncidentResponse response = new IncidentResponse(
                savedIncident.getId(),
                savedIncident.getLatitude(),
                savedIncident.getLongitude(),
                savedIncident.getCategory(),
                savedIncident.getSeverityScore(),
                savedIncident.getIncidentTime()
        );

        sseService.broadcast(response);

        return response;
    }

    public List<IncidentResponse> getActiveIncidents() {
        return incidentRepository.findByExpiresAtAfter(LocalDateTime.now())
                .stream().map(i -> new IncidentResponse(
                        i.getId(),
                        i.getLatitude(),
                        i.getLongitude(),
                        i.getCategory(),
                        i.getSeverityScore(),
                        i.getIncidentTime()
                )).toList();
    }
}
