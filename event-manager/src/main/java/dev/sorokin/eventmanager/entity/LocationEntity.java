package dev.sorokin.eventmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "locations")
@Getter
@Setter
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private Integer capacity;

    private String description;

    public LocationEntity(
            Long id,
            String name,
            String address,
            Integer capacity,
            String description
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.description = description;
    }

    public LocationEntity() {
    }
}
