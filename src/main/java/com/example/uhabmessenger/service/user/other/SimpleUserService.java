package com.example.uhabmessenger.service.user.other;

import com.example.uhabmessenger.exception.UserSaveException;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.repository.entity.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SimpleUserService {

    private final UserRepository userRepository;

    @Transactional
    public UserModel save(UserModel userModel) {

        try {
            return userRepository.save(userModel);
        } catch (Exception e) {
            throw new UserSaveException("error at user save");
        }

    }

    @Transactional(readOnly = true)
    public UserModel findById(UUID userId) {

        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("user not found by id"));

    }

    @Transactional(readOnly = true)
    public UserModel findByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("user not found by email"));

    }

    @Transactional(readOnly = true)
    public UserModel findByPhone(String phone) {

        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new EntityNotFoundException("user not found by phone"));

    }

    @Transactional(readOnly = true)
    public boolean isExistByUsername(String username) {

        return userRepository.existsByPhone(username)
                || userRepository.existsByEmail(username);

    }

}
