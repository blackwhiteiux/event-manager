package dev.sorokin.eventmanager.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DefaultUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createDefaultUser(
                "user",
                "user",
                21,
                UserRole.USER
        );

        createDefaultUser(
                "admin",
                "admin",
                22,
                UserRole.ADMIN
        );

    }


    private void createDefaultUser(
            String login,
            String password,
            Integer age,
            UserRole role
    ) {
        if(userRepository.existsByLogin(login)) {
            log.info("Пользователь {}  уже существует.", login);
            return;
        }

        var passwordHash = passwordEncoder.encode(password);
        var userToSave = new UserEntity(
                null,
                login,
                passwordHash,
                age,
                role
        );

        userRepository.save(userToSave);
        log.info("Пользователь с логином={} создан", login);
    }
}


