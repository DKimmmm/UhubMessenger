package com.example.uhabmessenger.service.user.other;

import com.example.uhabmessenger.exception.UserNotFoundException;
import com.example.uhabmessenger.exception.UserSaveException;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.repository.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SimpleUserService {

    private final UserRepository userRepository;

    public UserModel save(UserModel userModel) {

        try {
            return userRepository.save(userModel);
        } catch (Exception e) {
            throw new UserSaveException("error at user save");
        }

    }

    public UserModel findById(UUID userId) {

        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found by id"));

    }

    public UserModel findByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user not found by email"));

    }

    public UserModel findByPhone(String phone) {

        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new UserNotFoundException("user not found by phone"));

    }

    public void deleteUserModel(UserModel userModel) {

        userRepository.delete(userModel);

    }

}
