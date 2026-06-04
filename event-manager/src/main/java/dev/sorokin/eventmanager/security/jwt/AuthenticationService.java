package dev.sorokin.eventmanager.security.jwt;

import dev.sorokin.eventmanager.users.UserCredentials;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager tokenManager;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            JwtTokenManager tokenManager
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
    }

    public String authenticateUser(UserCredentials credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.login(),
                        credentials.password()
                )
        );

        return tokenManager.generateToken(credentials.login());
    }
}
