package com.example.uhabmessenger.controller.entity;

import com.example.uhabmessenger.dto.groups.GroupInfoDto;
import com.example.uhabmessenger.service.GroupService;
import lombok.RequiredArgsConstructor;
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

        groupService.save(title, description, userIds);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/all-info")
    public ResponseEntity<List<GroupInfoDto>> getAllInfo() {
        return ResponseEntity.ok(groupService.getAllInfo());
    }

    @GetMapping("/info/{groupId}")
    public ResponseEntity<GroupInfoDto> getInfo(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.getInfo(groupId));
    }

    @PostMapping("/add-members")
    public ResponseEntity<Void> addMembers(@RequestParam UUID groupId,
            @RequestBody List<UUID> membersIds) {
        groupService.addMembers(groupId, membersIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/member-remove")
    public ResponseEntity<Void> removeMember(@RequestParam UUID groupId,
            @RequestParam UUID memberId) {
        groupService.removeMember(groupId, memberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{groupId}")
    public ResponseEntity<Void> groupRemove(@PathVariable UUID groupId) {
        groupService.removeById(groupId);
        return ResponseEntity.ok().build();
    }
}
