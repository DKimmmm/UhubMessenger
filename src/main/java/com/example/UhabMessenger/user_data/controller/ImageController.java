package com.example.UhabMessenger.user_data.controller;

import com.example.UhabMessenger.user_data.service.MainService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("image")
public class ImageController {

    private final MainService mainService;

    @PostMapping(value = "create/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(@RequestPart MultipartFile multipartFile) throws Throwable {
        mainService.uploadImage(multipartFile);
        return ResponseEntity.ok().build();
    }

    @GetMapping("download")
    public ResponseEntity<?> downloadImage(@RequestParam String fileName, HttpServletResponse response) {
        try {
            mainService.downloadImage(fileName, response);
            return ResponseEntity.ok().build();
        } catch (Throwable e) {
            return new ResponseEntity<>(HttpStatus.valueOf(444));
        }
    }
}
