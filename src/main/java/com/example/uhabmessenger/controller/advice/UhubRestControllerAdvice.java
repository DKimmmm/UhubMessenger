package com.example.uhabmessenger.controller.advice;

import com.example.uhabmessenger.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UhubRestControllerAdvice {

    @ExceptionHandler(value = {AuthorizationErrorException.class, UserAlreadyExistsException.class, UsernameIncorrectException.class,
            GroupNotFoundException.class, PostNotFoundException.class, GroupSaveException.class})

    public ResponseEntity<String> handleAuthorizationErrorException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("К сожалению, произошла ошибка. Error: \n" + e.getMessage());
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(Exception e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("К сожалению, произошла ошибка поиска данных. Error: \n" + e.getMessage());

    }

    @ExceptionHandler(UncorrectedPasswordException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(Exception e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("К сожалению, произошла ошибка с паролем. Error: \n" + e.getMessage());
    }

    @ExceptionHandler(DownloadImageException.class)
    public ResponseEntity<String> handleDownloadImageException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("К сожалению, произошла ошибка получения изображения. \nError: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllException(Exception e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("К сожалению, произошла серьозная ошибка. Обратитесь к админестратору! \nError: " + e.getMessage());
    }

}
