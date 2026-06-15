package dev.sorokin.eventmanager.entity;

import dev.sorokin.eventmanager.domain.EventStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime startAt;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "max_places")
    private Integer maxPlaces;

    @Column(name = "occupied_places")
    private Integer occupiedPlaces;

    private Integer cost;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    @OneToMany(mappedBy = "event")
    private List<RegistrationEntity> registrations = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    public EventEntity() {
    }

    public EventEntity(
            Long id,
            String name,
            LocalDateTime startAt,
            Integer durationMinutes,
            Integer maxPlaces,
            Integer occupiedPlaces,
            EventStatus status
    ) {
        this.id = id;
        this.name = name;
        this.startAt = startAt;
        this.durationMinutes = durationMinutes;
        this.maxPlaces = maxPlaces;
        this.occupiedPlaces = occupiedPlaces;
        this.status = status;
    }
}
