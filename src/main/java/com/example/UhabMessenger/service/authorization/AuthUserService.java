package com.example.UhabMessenger.service.authorization;

import com.example.UhabMessenger.dto.LoginDto;
import com.example.UhabMessenger.dto.SignUpDto;

public interface AuthUserService {

    void signUp(SignUpDto signUpDto);

    void login(LoginDto loginDto);
}
