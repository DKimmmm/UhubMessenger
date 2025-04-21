package com.example.uhabmessenger.controller.entity;

import com.example.uhabmessenger.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/group")
public class GroupController {

    private final GroupService groupService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestParam String title,
                                    @RequestParam(required = false) String description,
                                    @RequestPart(required = false) List<UUID> userIds) {
        try {
            groupService.save(title, description, userIds);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all-info")
    public ResponseEntity<?> getAllInfo() {
        try {
            return ResponseEntity.ok(groupService.getAllInfo());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
