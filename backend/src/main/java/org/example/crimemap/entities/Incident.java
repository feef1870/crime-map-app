package org.example.crimemap.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.crimemap.enums.IncidentCategory;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
@Getter
@Setter
@NoArgsConstructor

public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",  nullable = false)
    @NotNull(message = "User id foreign key can not be null")
    private User user;

    @Column(name = "latitude")
    @NotNull(message = "Latitude can not be null")
    private Double latitude;

    @Column(name = "longitude")
    @NotNull(message = "Longitude can not be null")
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    @NotNull(message = "Category of the incident can not be null")
    private IncidentCategory category;

    @Column(name = "severity_score")
    @NotNull(message = "Severity score can not be null")
    @Range(min = 1, max = 5, message = "Severity score must be between 1 and 5")
    private Integer severityScore;

    @Column(name = "incident_time")
    @NotNull(message = "Incident time can not be null")
    @PastOrPresent(message = "Incident time can not be in the future")
    private LocalDateTime incidentTime;

    @Column(name = "reported_at")
    @NotNull(message = "Report time can not be null")
    private LocalDateTime reportedAt;

    @Column(name = "expires_at")
    @NotNull(message = "Expiry date can not be null")
    private LocalDateTime expiresAt;

    @PrePersist
    public void preInsert() {
        if (this.reportedAt == null) {
            this.reportedAt = LocalDateTime.now();
        }

        if (this.incidentTime != null) {
            this.expiresAt = this.incidentTime.plusMinutes(15);
        }
    }
}
