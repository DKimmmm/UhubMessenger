package com.example.UhabMessenger.authentication.service.authorization;

import com.example.UhabMessenger.authentication.secutiry.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenInjectionService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;


    public void jwtInjection(HttpServletResponse response, String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        response.addHeader("Authorization", "Bearer " + jwt);
    }

}
