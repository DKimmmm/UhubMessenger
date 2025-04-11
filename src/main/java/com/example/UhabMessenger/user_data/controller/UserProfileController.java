package com.example.UhabMessenger.user_data.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("user-profile")
public class UserProfileController {

    @PostMapping
    public ResponseEntity<?> testTokenMapping(@RequestBody String message) {
        return ResponseEntity.ok(message);
    }

}
