package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.dto.JwtResponse;
import dev.sorokin.eventmanager.mapper.UserMapper;
import dev.sorokin.eventmanager.security.jwt.AuthenticationService;
import dev.sorokin.eventmanager.dto.UserCredentials;
import dev.sorokin.eventmanager.dto.UserDto;
import dev.sorokin.eventmanager.dto.UserRegistration;
import dev.sorokin.eventmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;

    public UserController(
            UserService userService,
            UserMapper userMapper,
            AuthenticationService authenticationService
    ) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Valid UserRegistration userRegistration
    ) {
        log.info("POST /users - request to register user with login: {}", userRegistration.login());

        var user = userService.registerUser(userRegistration);
        var response = userMapper.toDto(user);

        log.info("POST /users - user registered successfully with id: {}", response.id());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.toDto(user));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtResponse> authenticateUser(
            @RequestBody @Valid UserCredentials credentials
    ) {
        log.info("POST /users/auth - authentication request for login: {}", credentials.login());

        var jwtToken = authenticationService.authenticateUser(credentials);

        log.info("POST /users/auth - authentication successful for login: {}", credentials.login());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtResponse(jwtToken));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable Long id
    ) {
        log.info("GET /users/{} - request to get user by id", id);

        var user = userService.findUserById(id);
        var response = userMapper.toDto(user);

        log.info("GET /users/{} - found user with login: {}", id, response.login());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        userMapper.toDto(user)
                );
    }
}
