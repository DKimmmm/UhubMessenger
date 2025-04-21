package com.example.uhabmessenger.service.user.authorization;

import com.example.uhabmessenger.dto.register.LoginDto;
import com.example.uhabmessenger.dto.register.SignUpDto;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

public interface AuthUserService {

    void signup(SignUpDto signUpDto, HttpServletResponse response);

    UUID login(LoginDto loginDto, HttpServletResponse response);
}
