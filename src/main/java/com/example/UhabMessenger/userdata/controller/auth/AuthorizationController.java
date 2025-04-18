package com.example.UhabMessenger.userdata.controller.auth;

import com.example.UhabMessenger.userdata.dto.register.LoginDto;
import com.example.UhabMessenger.userdata.dto.register.SignUpDto;
import com.example.UhabMessenger.userdata.exception.AuthorizationErrorException;
import com.example.UhabMessenger.userdata.exception.UncorrectedPasswordException;
import com.example.UhabMessenger.userdata.exception.UserAlreadyExistsException;
import com.example.UhabMessenger.userdata.service.user.authorization.AuthUserService;
import com.example.UhabMessenger.authentication.statusCodes.HttpCodes;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/authorization")
@RequiredArgsConstructor

public class AuthorizationController {

    private final AuthUserService authUserService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpDto signUpDto, HttpServletResponse response) {
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
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {
        try {
            return ResponseEntity.ok(authUserService.login(loginDto, response));
        } catch (UncorrectedPasswordException e) {
            return new ResponseEntity<>(HttpStatus.valueOf(
                    HttpCodes.UncorrectedPassword.getCode()
            ));
        } catch (AuthorizationErrorException e) {
            return new ResponseEntity<>(HttpStatus.valueOf(
                    HttpCodes.AuthorizationError.getCode()
            ));
        }
    }

}
