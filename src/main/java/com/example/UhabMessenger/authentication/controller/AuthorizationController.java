package com.example.UhabMessenger.authentication.controller;

import com.example.UhabMessenger.authentication.dto.LoginDto;
import com.example.UhabMessenger.authentication.dto.SignUpDto;
import com.example.UhabMessenger.authentication.exception.AuthorizationErrorException;
import com.example.UhabMessenger.authentication.exception.UncorrectedPasswordException;
import com.example.UhabMessenger.authentication.exception.UserAlreadyExistsException;
import com.example.UhabMessenger.authentication.service.authorization.AuthUserService;
import com.example.UhabMessenger.authentication.statusCodes.HttpCodes;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorization")
@RequiredArgsConstructor

public class AuthorizationController {

    private final AuthUserService authUserService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(SignUpDto signUpDto, HttpServletResponse response) {

        try {
            authUserService.signup(signUpDto, response);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(
                    HttpStatus.valueOf(
                            HttpCodes.AlreadyUserExists.getCode()
                    ));
        } catch (AuthorizationErrorException e) {
            return new ResponseEntity<>(
                    HttpStatus.valueOf(
                            HttpCodes.AuthorizationError.getCode()
                    ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(LoginDto loginDto, HttpServletResponse response) {
        try {
            return ResponseEntity.ok(authUserService.login(loginDto, response));
        } catch (UncorrectedPasswordException e) {
            return new ResponseEntity<>(HttpStatus.valueOf(
                    HttpCodes.UncorrectedPassword.getCode()
            ));
        }
    }

}
