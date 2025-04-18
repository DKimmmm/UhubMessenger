package com.example.UhabMessenger.userdata.controller;

import com.example.UhabMessenger.userdata.dto.user.UserInfoDto;
import com.example.UhabMessenger.userdata.service.user.main.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> testTokenMapping(@RequestBody String message) {
        return ResponseEntity.ok(message);
    }

    @SneakyThrows
    @PostMapping(value = "/add-image/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> save(@PathVariable UUID userId,
                                     @RequestPart MultipartFile multipartFile) {
        try {
            userService.uploadUserImage(multipartFile, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.valueOf(414));
        }
    }

    @DeleteMapping("/image")
    public ResponseEntity<?> deleteImage(@RequestParam UUID userId,
                                         @RequestParam UUID imageId) {
        try {
            userService.deleteImage(userId, imageId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.valueOf(414));
        }

    }

    @GetMapping("/image-download")
    public ResponseEntity<Void> downloadImage(@RequestParam UUID userId, @RequestParam UUID imageId, HttpServletResponse response) {
        try {
            userService.downloadImageByImageAndUserIds(imageId, userId, response);
            return ResponseEntity.ok().build();
        } catch (Throwable e) {
            return new ResponseEntity<>(HttpStatus.valueOf(415));
        }
    }

    @GetMapping("/user-info/{userId}")
    public ResponseEntity<UserInfoDto> getUserInfo(@PathVariable UUID userId) {
        try {
            return ResponseEntity.ok(userService.getUserInfo(userId));
        } catch (Throwable e) {
            return new ResponseEntity<>(HttpStatus.valueOf(414));
        }
    }

    @GetMapping("/my-posts/{userId}")
    public ResponseEntity<?> getMyPostsInfo(@PathVariable UUID userId) {
        try {
            return ResponseEntity.ok(userService.findPostsInfoListByUserId(userId));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
