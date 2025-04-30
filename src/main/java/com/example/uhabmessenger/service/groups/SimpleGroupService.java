package com.example.uhabmessenger.service.groups;

import com.example.uhabmessenger.model.GroupModel;
import com.example.uhabmessenger.repository.entity.GroupRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimpleGroupService {

    private final GroupRepository groupRepository;

    @Transactional(readOnly = true)
    public GroupModel findById(UUID groupId) {

        return groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new EntityNotFoundException("group not found by ID: " + groupId));

    }

    @Transactional
    public void save(GroupModel groupModel) {

        groupRepository.save(groupModel);

    }

    @Transactional(readOnly = true)
    public boolean existsByTitle(String title) {

        return groupRepository.existsByTitle(title);

    }

    @Transactional(readOnly = true)
    public List<GroupModel> findAll() {

        return groupRepository.findAll();

    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void deleteById(UUID groupId) {

        groupRepository.deleteById(groupId);

    }
}