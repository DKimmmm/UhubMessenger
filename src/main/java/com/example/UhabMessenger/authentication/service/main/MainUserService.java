package com.example.UhabMessenger.authentication.service.main;

import com.example.UhabMessenger.authentication.model.UserModel;
import com.example.UhabMessenger.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MainUserService {

    private static final Logger log = LoggerFactory.getLogger(MainUserService.class);
    private final UserRepository userRepository;

    public UserModel getUserByUsername(String username) {
        log.info("add custom exception");
        if (usernameIsEmailFormat()) {
            return findUserByEmail(username);
        } else if (usernameIsPhoneFormat()) {
            return findUserByPhone(username);
        } else {
            throw new RuntimeException();
        }
    }

    private UserModel findUserByEmail(String username) {
        return userRepository.findByEmail(username).get();
    }
    private UserModel findUserByPhone(String username) {
        return userRepository.findByPhone(username).get();
    }

    private boolean usernameIsPhoneFormat() {
        log.info("add logic");
        return true;
    }

    private boolean usernameIsEmailFormat() {
        log.info("add logic");
        return false;
    }
}
