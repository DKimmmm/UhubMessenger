package com.example.UhabMessenger.authentication.service.authorization;

import com.example.UhabMessenger.authentication.dto.LoginDto;
import com.example.UhabMessenger.authentication.exception.AuthorizationErrorException;
import com.example.UhabMessenger.authentication.exception.UncorrectedPasswordException;
import com.example.UhabMessenger.authentication.exception.UserAlreadyExistsException;
import com.example.UhabMessenger.authentication.dto.SignUpDto;
import com.example.UhabMessenger.authentication.mapper.MapstructService;
import com.example.UhabMessenger.authentication.model.UserModel;
import com.example.UhabMessenger.authentication.repository.UserRepository;
import com.example.UhabMessenger.authentication.service.main.MainUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private static final Logger log = LoggerFactory.getLogger(AuthUserServiceImpl.class);
    private final MapstructService mapstructService;

    private final UserRepository userRepository;
    private final MainUserService userService;

    @Override
    @Transactional
    public void signup(SignUpDto signUpDto, HttpServletResponse response) {
        try {
            if (checkForAlreadyExists(signUpDto.username())) {
                throw new UserAlreadyExistsException("user this username "+ signUpDto + " already exists");
            }
            userRepository.save(mapperToUserModel(signUpDto));
        } catch (UserAlreadyExistsException | AuthorizationErrorException e){
            log.warn("was be error {}", e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UUID login(LoginDto loginDto, HttpServletResponse response) {
        if (!checkForAlreadyExists(loginDto.username())) {
            throw new UncorrectedPasswordException("user login fail with username: " + loginDto.username());
        }
        return findIdByUsername(loginDto.username());
    }

    private UUID findIdByUsername(String username) {
        return userService.getUserByUsername(username).getUserId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private boolean checkForAlreadyExists(String username) {
        return userRepository.existsByPhone(username) || userRepository.existsByEmail(username);
    }

    private UserModel mapperToUserModel(SignUpDto signUpDto) {
        return addUsername(
                mapstructService.toUserModel(signUpDto),
                signUpDto.username()
        );
    }

    private UserModel addUsername(UserModel userModel, String username) {
        log.info("add custom exception");
        if (usernameIsEmailFormatted()) {
            userModel.setEmail(username);
        } else if (usernameIsPhoneFormatted()) {
            userModel.setPhone(username);
        } else {
            throw new RuntimeException();
        }
        return userModel;
    }

    private boolean usernameIsPhoneFormatted() {
        log.info("add check is this email or phone");
        return true;
    }

    private boolean usernameIsEmailFormatted() {
        log.info("add check is this email or phone");
        return false;
    }
}
