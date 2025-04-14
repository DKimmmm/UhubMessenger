package com.example.UhabMessenger.user_data.controller;

import com.example.UhabMessenger.user_data.service.MainService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final MainService mainService;

    @SneakyThrows
    @PostMapping(value = "/create/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(@PathVariable UUID userId,
                                  @RequestPart MultipartFile multipartFile) {
        mainService.uploadImage(multipartFile, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download")
    public ResponseEntity<Void> downloadImage(@RequestParam UUID userId, HttpServletResponse response) {
        try {
            mainService.downloadImage(userId, response);
            return ResponseEntity.ok().build();
        } catch (Throwable e) {
            return new ResponseEntity<>(HttpStatus.valueOf(415));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteImage(@RequestParam UUID userId) {
        try {
            mainService.deleteImage(userId);
            return ResponseEntity.ok().build();
        } catch (Throwable e) {
            return new ResponseEntity<>(HttpStatus.valueOf(414));
        }
    }
}
