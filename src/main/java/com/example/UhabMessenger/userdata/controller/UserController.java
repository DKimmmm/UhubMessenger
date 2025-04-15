package com.example.UhabMessenger.userdata.controller;

import com.example.UhabMessenger.userdata.service.user.main.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> testTokenMapping(@RequestBody String message) {
        return ResponseEntity.ok(message);
    }

    @SneakyThrows
    @PostMapping(value = "/create/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(@PathVariable UUID userId,
                                  @RequestPart MultipartFile multipartFile) {
        userService.uploadUserImage(multipartFile, userId);
        return ResponseEntity.ok().build();
    }
}
