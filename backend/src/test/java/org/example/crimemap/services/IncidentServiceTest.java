package org.example.crimemap.services;

import org.example.crimemap.dto.IncidentRequest;
import org.example.crimemap.dto.IncidentResponse;
import org.example.crimemap.entities.Incident;
import org.example.crimemap.entities.User;
import org.example.crimemap.enums.IncidentCategory;
import org.example.crimemap.repositories.IncidentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IncidentServiceTest {
    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private SseService sseService;

    @InjectMocks
    private IncidentService incidentService;

    @Test
    void shouldCreateIncidentAndBroadcast() {
        IncidentRequest request = new IncidentRequest(
                51.2464,
                22.5621,
                IncidentCategory.THEFT,
                5,
                LocalDateTime.now()
        );

        User user = new User();
        user.setId(34L);
        user.setEmail("abcd@gmail.com");
        user.setPasswordHash("1234");
        user.setCreatedAt(LocalDateTime.now());
        user.setTrustScore(5.0);

        Incident savedEntity = new Incident();
        savedEntity.setId(42L);
        savedEntity.setLatitude(request.latitude());
        savedEntity.setLongitude(request.longitude());
        savedEntity.setCategory(request.category());
        savedEntity.setSeverityScore(request.severityScore());
        savedEntity.setIncidentTime(request.incidentTime());
        savedEntity.setUser(user);

        when(incidentRepository.save(any(Incident.class))).thenReturn(savedEntity);

        IncidentResponse response = incidentService.createIncident(request, user);

        assertNotNull(response, "Response should not be null");
        assertEquals(42L, response.id(), "The id should match the generated ID");
        assertEquals(IncidentCategory.THEFT, response.category(), "The category should map correctly");

        verify(incidentRepository, times(1)).save(any(Incident.class));

        verify(sseService, times(1)).broadcast(any(IncidentResponse.class));
    }
}
