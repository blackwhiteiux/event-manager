package dev.sorokin.eventmanager.security.jwt;

import dev.sorokin.eventmanager.users.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;

    public JwtTokenFilter(
            JwtTokenManager jwtTokenManager,
            @Lazy UserService userService
    ) {
        this.jwtTokenManager = jwtTokenManager;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationToken = authorizationHeader.substring(7);

        String loginFromToken;
        try {
            loginFromToken = jwtTokenManager.getLoginFromToken(authorizationToken);
        } catch (Exception e) {
            log.error("Ошибка во время чтения JWT токена", e);
            filterChain.doFilter(request, response);
            return;
        }

        var user = userService.findUserByLogin(loginFromToken);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user,
                null,
                List.of(new SimpleGrantedAuthority(user.role().toString()))
        );

        SecurityContextHolder.getContext()
                .setAuthentication(token);
        filterChain.doFilter(request, response);
    }
}
