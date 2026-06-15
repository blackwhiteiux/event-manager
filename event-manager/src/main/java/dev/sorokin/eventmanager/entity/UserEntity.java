package dev.sorokin.eventmanager.entity;

import dev.sorokin.eventmanager.domain.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    @Column(name = "password_hash")
    private String passwordHash;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user")
    private List<RegistrationEntity> registrations = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<EventEntity> events;

    public UserEntity() {
    }

    public UserEntity(
            Long id,
            String login,
            String passwordHash,
            Integer age,
            UserRole role
    ) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
        this.age = age;
        this.role = role;
    }
}
