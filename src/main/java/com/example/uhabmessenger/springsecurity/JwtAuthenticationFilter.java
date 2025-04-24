package com.example.uhabmessenger.springsecurity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request);
        log.debug("api is: [{}], token is: [{}]", request.getServletPath(), token);
        token = tokenBearerDelete(token);

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.debug("token isn't validated");
            }
        } catch (Exception e) {
            log.warn("error in check token");
        }

        filterChain.doFilter(request, response);
    }

    private String tokenBearerDelete(String token) {
        if (token == null) {
            return null; // Или выбросить исключение, если null недопустим
        }

        String bearerPrefix = "Bearer ";
        if (token.startsWith(bearerPrefix)) {
            return token.substring(bearerPrefix.length());
        }

        return token;
    }
}

