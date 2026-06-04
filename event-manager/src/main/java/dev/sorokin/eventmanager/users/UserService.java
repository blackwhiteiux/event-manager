package dev.sorokin.eventmanager.users;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public User registerUser(
            UserRegistration userRegistration
    ) {
        if(userRepository.existsByLogin(userRegistration.login())) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует.");
        }

        var hashedPass = passwordEncoder.encode(userRegistration.password());

        var userToSave = new UserEntity(
                null,
                userRegistration.login(),
                hashedPass,
                userRegistration.age(),
                UserRole.USER
        );

        var savedUserEntity = userRepository.save(userToSave);

        return userMapper.toDomain(savedUserEntity);
    }

    public User findUserByLogin(String login) {
        return userMapper.toDomain(
                userRepository.findByLogin(login)
                        .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"))
        );
    }

    public User findUserById(
            Long id
    ) {
        return userMapper.toDomain(
                userRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"))
        );
    }
}
