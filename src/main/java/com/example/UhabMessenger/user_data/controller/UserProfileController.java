package com.example.UhabMessenger.user_data.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test-access")
public class UserProfileController {

    @PostMapping
    public ResponseEntity<?> testTokenMapping(@RequestBody String message) {
        return ResponseEntity.ok(message);
    }
}
