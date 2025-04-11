package com.example.UhabMessenger.authentication.service.authorization;

import com.example.UhabMessenger.authentication.dto.LoginDto;
import com.example.UhabMessenger.authentication.dto.SignUpDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthUserService {

    void signup(SignUpDto signUpDto, HttpServletResponse response);

    void login(LoginDto loginDto, HttpServletResponse response);
}
