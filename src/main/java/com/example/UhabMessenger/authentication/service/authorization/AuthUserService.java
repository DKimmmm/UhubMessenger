package com.example.UhabMessenger.authentication.service.authorization;

import com.example.UhabMessenger.authentication.dto.LoginDto;
import com.example.UhabMessenger.authentication.dto.SignUpDto;

public interface AuthUserService {

    void signUp(SignUpDto signUpDto);

    void login(LoginDto loginDto);
}
