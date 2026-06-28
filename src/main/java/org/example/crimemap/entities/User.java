package org.example.crimemap.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    @NotBlank(message = "Email can not be blank")
    private String email;

    @Column(name = "password_hash")
    @NotBlank(message = "Password hash is blank!")
    private String passwordHash;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "trust_score")
    @Range(min = 0, max = 5, message = "Trust score must be between 0 and 5")
    private Double trustScore;

    @PrePersist
    public void preInsert() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }

        if (this.trustScore == null) {
            this.trustScore = 5.0;
        }
    }
}
