package dev.sorokin.eventmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
@Getter
@Setter
public class RegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    public RegistrationEntity() {
    }

    public RegistrationEntity(
            Long id,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.createdAt = createdAt;
    }
}
