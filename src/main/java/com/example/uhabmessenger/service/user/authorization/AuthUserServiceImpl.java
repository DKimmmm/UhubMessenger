package com.example.uhabmessenger.service.user.authorization;

import com.example.uhabmessenger.dto.register.LoginDto;
import com.example.uhabmessenger.dto.register.SignUpDto;
import com.example.uhabmessenger.exception.AuthorizationException;
import com.example.uhabmessenger.mapper.UserMapstructService;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.service.user.other.SimpleUserService;
import com.example.uhabmessenger.service.user.other.UserService;
import com.example.uhabmessenger.validation.fieldFormat.PhoneOrEmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final UserMapstructService userMapstructService;

    private final UserService userService;

    private final SimpleUserService simpleUserService;

    @Override
    @Transactional
    public void signup(SignUpDto signUpDto) {

        if (checkForAlreadyExists(signUpDto.username())) {
            throw new AuthorizationException("user this username " + signUpDto + " already exists");
        }
        simpleUserService.save(mapperToUserModel(signUpDto));

    }

    @Override
    @Transactional(readOnly = true)
    public UUID login(LoginDto loginDto) {

        if (!checkForAlreadyExists(loginDto.username())) {
            throw new AuthorizationException("user login fail with username: " + loginDto.username());
        }
        return findIdByUsername(loginDto.username());

    }

    private UUID findIdByUsername(String username) {

        return userService.getUserByUsername(username).getUserId();

    }

    private boolean checkForAlreadyExists(String username) {

        return simpleUserService.isExistByUsername(username);

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
            throw new AuthorizationException("wrong username");
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
