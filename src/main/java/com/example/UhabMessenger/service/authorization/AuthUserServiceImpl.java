package com.example.UhabMessenger.service.authorization;

import com.example.UhabMessenger.dto.LoginDto;
import com.example.UhabMessenger.exception.UncorrectedPasswordException;
import com.example.UhabMessenger.exception.UserAlreadyExistsException;
import com.example.UhabMessenger.dto.SignUpDto;
import com.example.UhabMessenger.model.UserModel;
import com.example.UhabMessenger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private static final Logger log = LoggerFactory.getLogger(AuthUserServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void signUp(SignUpDto signUpDto) {
        try {
            if (checkForAlreadyExists(signUpDto.username())) {
                throw new UserAlreadyExistsException("user this username "+ signUpDto + " already exists");
            }
            userRepository.save(mapperToUserModel(signUpDto));
        } catch (UserAlreadyExistsException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public void login(LoginDto loginDto) {
        if (!checkForAlreadyExists(loginDto.username())) {
            throw new UncorrectedPasswordException("user login fail with username: " + loginDto.username());
        }
    }

    private boolean checkForAlreadyExists(String username) {
        return userRepository.existsByPhone(username) || userRepository.existsByEmail(username);
    }

    private UserModel mapperToUserModel(SignUpDto signUpDto) {
        UserModel userModel = new UserModel(
                signUpDto.name(),
                signUpDto.lastname(),
                signUpDto.password(),
                false,
                false
        );

        return addUsername(userModel, signUpDto.username());
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
