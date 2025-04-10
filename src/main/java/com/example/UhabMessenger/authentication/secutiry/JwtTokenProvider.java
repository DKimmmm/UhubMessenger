package com.example.UhabMessenger.authentication.secutiry;

import com.example.UhabMessenger.authentication.model.UserModel;
import com.example.UhabMessenger.authentication.service.main.MainUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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
    private final long JWT_EXPIRATION = 604800000L; // Время жизни токена - 7 дней
//    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    private final String JWT_SECRET = "yoursdfpauhg;lsdhghasidfhghpash[dfoahwedfskjajdsfnv;akldfhjadshfkjj";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

    private final MainUserService userService;

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return Jwts.builder()
                .subject(userPrincipal.getUsername())
//                .issuedAt(new Date())
//                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(secretKey)
                .compact();
    }

    //извлекаем токен из заголовка
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
    // check token
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
        String username = claims.getSubject(); //извлечение ключегого поля элемента
        // поиск user and his password
        UserModel userModel = userService.getUserByUsername(username);
//        log.info("username in getAuthentication is {}", login);

        String currentPassword = userModel.getPassword();

        String encodePassword = passwordEncoder.encode(currentPassword);
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        UserPrincipal userPrincipal = new UserPrincipal(username, encodePassword, authorities);

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, "USER_ROLE", userPrincipal.getAuthorities());
        return authenticationToken;
    }
}

