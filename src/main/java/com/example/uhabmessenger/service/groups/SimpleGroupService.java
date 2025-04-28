package com.example.uhabmessenger.service.groups;

import com.example.uhabmessenger.model.GroupModel;
import com.example.uhabmessenger.repository.entity.GroupRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimpleGroupService {

    private final GroupRepository groupRepository;

    public GroupModel findById(UUID groupId) {

        return groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new EntityNotFoundException("group not found by ID: " + groupId));

    }

    public void save(GroupModel groupModel) {

        groupRepository.save(groupModel);

    }

    public boolean existsByTitle(String title) {

        return groupRepository.existsByTitle(title);

    }

    public List<GroupModel> findAll() {

        return groupRepository.findAll();

    }

    public void deleteById(UUID groupId) {

        groupRepository.deleteById(groupId);

    }
}