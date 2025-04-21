package com.example.uhabmessenger.controller;

import com.example.uhabmessenger.service.ImageService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

//    @SneakyThrows
//    @PostMapping(value = "/create/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> save(@PathVariable UUID userId,
//                                  @RequestPart MultipartFile multipartFile) {
////        imageService.uploadImage(multipartFile, userId);
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/download")
    public ResponseEntity<Void> downloadImage(@RequestParam UUID userId, HttpServletResponse response) {
        try {
            imageService.downloadImage(userId, response);
            return ResponseEntity.ok().build();
        } catch (Throwable e) {
            return new ResponseEntity<>(HttpStatus.valueOf(415));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteImage(@RequestParam UUID userId) {
        try {
            imageService.deleteImage(userId);
            return ResponseEntity.ok().build();
        } catch (Throwable e) {
            return new ResponseEntity<>(HttpStatus.valueOf(414));
        }
    }
}
