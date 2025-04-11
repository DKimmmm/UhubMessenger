package com.example.UhabMessenger.authentication.service.authorization;

import com.example.UhabMessenger.authentication.dto.LoginDto;
import com.example.UhabMessenger.authentication.dto.SignUpDto;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

public interface AuthUserService {

    void signup(SignUpDto signUpDto, HttpServletResponse response);

    UUID login(LoginDto loginDto, HttpServletResponse response);
}
