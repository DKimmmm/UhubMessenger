package com.example.uhabmessenger.controller.entity;

import com.example.uhabmessenger.dto.groups.GroupCreateDto;
import com.example.uhabmessenger.dto.groups.GroupInfoDto;
import com.example.uhabmessenger.dto.posts.PostInfoDto;
import com.example.uhabmessenger.service.groups.GroupService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/group")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid GroupCreateDto groupCreateDto) {

        groupService.save(groupCreateDto);
        return ResponseEntity.ok().build();

    }

    @PostMapping(value = "/photo-update/{groupOrUserId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> photoUpdate(@PathVariable UUID groupId,
                                            @RequestPart MultipartFile multipartFile) {
        groupService.photoUpdate(groupId, multipartFile);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/all-info")
    public ResponseEntity<List<GroupInfoDto>> getAllInfo() {
        return ResponseEntity.ok(groupService.getAllInfo());
    }

    @GetMapping("/info/{groupOrUserId}")
    public ResponseEntity<GroupInfoDto> getInfo(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.getInfo(groupId));
    }

    @PostMapping("/add-members")
    public ResponseEntity<Void> addMembers(@RequestParam UUID groupId,
                                           @RequestBody List<UUID> membersIds) {
        groupService.addMembers(groupId, membersIds);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/member")
    public ResponseEntity<Void> removeMember(@RequestParam UUID groupId,
                                             @RequestParam UUID memberId) {
        groupService.removeMember(groupId, memberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupOrUserId}")
    public ResponseEntity<Void> groupRemove(@PathVariable UUID groupId) {

        groupService.removeById(groupId);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/image/download")
    public ResponseEntity<Void> downloadImage(@RequestParam UUID groupId,
                                              @RequestParam UUID imageId,
                                              HttpServletResponse response) {

        groupService.downloadImage(imageId, groupId, response);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/posts/{groupOrUserId}")
    public ResponseEntity<List<PostInfoDto>> groupPostInfoDto(@PathVariable UUID groupId) {

        return ResponseEntity.ok(
                groupService.getPostsInfoDto(groupId)
        );

    }
}
