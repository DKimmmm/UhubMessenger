package com.example.UhabMessenger.userdata.service.user.other;

import com.example.UhabMessenger.userdata.exception.UserNotFoundException;
import com.example.UhabMessenger.userdata.model.UserModel;
import com.example.UhabMessenger.userdata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimpleSaveAndFindUserService {

    private final UserRepository userRepository;

    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
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

}
