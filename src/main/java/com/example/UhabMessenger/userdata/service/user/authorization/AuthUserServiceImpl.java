package com.example.UhabMessenger.userdata.service.user.authorization;

import com.example.UhabMessenger.userdata.dto.register.LoginDto;
import com.example.UhabMessenger.authentication.exception.AuthorizationErrorException;
import com.example.UhabMessenger.authentication.exception.UncorrectedPasswordException;
import com.example.UhabMessenger.authentication.exception.UserAlreadyExistsException;
import com.example.UhabMessenger.userdata.dto.register.SignUpDto;
import com.example.UhabMessenger.userdata.mapper.UserMapstructService;
import com.example.UhabMessenger.userdata.model.UserModel;
import com.example.UhabMessenger.userdata.repository.UserRepository;
import com.example.UhabMessenger.userdata.service.user.main.UserService;
import com.example.UhabMessenger.authentication.validation.PhoneOrEmailValidator;
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
    private final UserMapstructService userMapstructService;

    private final UserRepository userRepository;
    private final UserService userService;

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
                userMapstructService.toUserModel(signUpDto),
                signUpDto.username()
        );
    }

    private UserModel addUsername(UserModel userModel, String username) {
        if (usernameIsEmailFormatted(username)) {
            userModel.setEmail(username);
        } else if (usernameIsPhoneFormatted(username)) {
            userModel.setPhone(username);
        } else {
            throw new AuthorizationErrorException("wrong username");
        }
        return userModel;
    }

    private boolean usernameIsPhoneFormatted(String username) {
        return PhoneOrEmailValidator.matchersPhoneRegex(username);
    }

    private boolean usernameIsEmailFormatted(String username) {
        return PhoneOrEmailValidator.matchersEmailRegex(username);
    }
}
