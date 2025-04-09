package com.example.UhabMessenger.controller;

import com.example.UhabMessenger.dto.LoginDto;
import com.example.UhabMessenger.exception.AuthorizationErrorException;
import com.example.UhabMessenger.exception.UncorrectedPasswordException;
import com.example.UhabMessenger.exception.UserAlreadyExistsException;
import com.example.UhabMessenger.dto.SignUpDto;
import com.example.UhabMessenger.service.authorization.AuthUserService;
import com.example.UhabMessenger.statusCodes.HttpCodes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("authorization")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthUserService authUserService;

    @PostMapping("signUp")
    public ResponseEntity<Void> signUp(SignUpDto signUpDto) {
        try {
            authUserService.signUp(signUpDto);
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

    @PostMapping("login")
    public ResponseEntity<?> login(LoginDto loginDto) {
        try {
            authUserService.login(loginDto);
            return ResponseEntity.ok().build();
        } catch (UncorrectedPasswordException e) {
            return new ResponseEntity<>(HttpStatus.valueOf(
                    HttpCodes.UncorrectedPassword.getCode()
            ));
        }
    }

}
