package com.example.uhabmessenger.controller.entity;

import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.dto.user.UserInfoDto;
import com.example.uhabmessenger.dto.user.UserUpdateInfoDto;
import com.example.uhabmessenger.service.user.other.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @SneakyThrows
    @PostMapping(value = "/image/add/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> save(@PathVariable UUID userId,
                                     @RequestPart MultipartFile multipartFile) {

        userService.uploadUserImage(multipartFile, userId);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/image/download")
    public ResponseEntity<Void> downloadImage(@RequestParam UUID userId,
                                              @RequestParam UUID imageId,
                                              HttpServletResponse response) {

        userService.downloadImageByImageAndUserIds(imageId, userId, response);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/image")
    public ResponseEntity<Void> deleteImage(@RequestParam UUID userId,
                                            @RequestParam UUID imageId) {

        userService.deleteImage(userId, imageId);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/update/info")
    public ResponseEntity<UserInfoDto> updateInfo(@Valid @RequestBody UserUpdateInfoDto userUpdateInfoDto) {

        return ResponseEntity.ok(
                userService.updateInfo(userUpdateInfoDto)
        );

    }

    @GetMapping("/info/{userId}")
    public ResponseEntity<UserInfoDto> getUserInfo(@PathVariable UUID userId) {

        return ResponseEntity.ok(
                userService.getUserInfo(userId)
        );

    }

    @GetMapping("/posts/{userId}")
    public ResponseEntity<List<PostInfoDto>> getMyPostsInfo(@PathVariable UUID userId) {

        return ResponseEntity.ok(
                userService.findPostsInfoListByUserId(userId)
        );

    }
}
