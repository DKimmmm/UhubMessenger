package com.example.uhabmessenger.springsecurity;

import com.example.uhabmessenger.service.user.other.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    /**
     * Время жизни токена - 7 дней
     */
    private final long JWT_EXPIRATION = 604800000L;
    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    private final UserService userService;

    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(secretKey)
                .compact();

    }

    public String resolveToken(HttpServletRequest request) {

        return request.getHeader("Authorization");

    }

    public boolean validateToken(String token) {

        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return true;

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Authentication getAuthentication(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String username = claims.getSubject();
        String currentPassword = userService.getUserByUsername(username).getPassword();
        String encodePassword = passwordEncoder.encode(currentPassword);

        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        UserPrincipal userPrincipal = new UserPrincipal(username, encodePassword, authorities);

        return new UsernamePasswordAuthenticationToken(
                userPrincipal, "USER_ROLE", userPrincipal.getAuthorities()
        );

    }
}

