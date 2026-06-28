package org.example.crimemap.repositories;

import org.example.crimemap.entities.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByExpiresAtAfter(LocalDateTime time);
}
