package dev.sorokin.eventmanager.entity;

import dev.sorokin.eventmanager.domain.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
