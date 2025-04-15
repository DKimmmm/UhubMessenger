package com.example.UhabMessenger.userdata.service.user.authorization;

import com.example.UhabMessenger.userdata.dto.register.LoginDto;
import com.example.UhabMessenger.userdata.dto.register.SignUpDto;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

public interface AuthUserService {

    void signup(SignUpDto signUpDto, HttpServletResponse response);

    UUID login(LoginDto loginDto, HttpServletResponse response);
}
