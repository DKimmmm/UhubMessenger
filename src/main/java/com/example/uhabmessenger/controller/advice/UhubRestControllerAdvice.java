package com.example.uhabmessenger.controller.advice;

import com.example.uhabmessenger.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RestControllerAdvice
public class UhubRestControllerAdvice {

    @ExceptionHandler(value = {AuthorizationErrorException.class, UserAlreadyExistsException.class,
            UsernameIncorrectException.class, UncorrectedPasswordException.class})
    public ResponseEntity<String> handleAuthorizationException(Exception e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("К сожалению, произошла ошибка во время авторизации. Error: \n" + e.getMessage());

    }

    @ExceptionHandler(value = {UserNotFoundException.class, GroupNotFoundException.class, PostNotFoundException.class})
    public ResponseEntity<String> handleResourceNotFoundException(Exception e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Искомый ресурс не найден. Error: \n" + e.getMessage());

    }

    @ExceptionHandler(value = {GroupSaveException.class, ImageSaveException.class, DownloadImageException.class})
    public ResponseEntity<String> handleOtherException(Exception e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("К сожалению, произошла ошибка. Error: \n" + e.getMessage());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleNoValidDataException(MethodArgumentNotValidException e) {

        return ResponseEntity
                .status(HttpStatus.valueOf(401))
                .body("Получены данные некорректного формата, попробуйте снова. \nError: " + e.getMessage());

    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientException(HttpClientErrorException e) {

        return ResponseEntity
                .status(HttpStatus.valueOf(403))
                .body("Доступ закрыт. " +
                        "\nЛибо ограничен доступ, либо не предоставлены данные для аутентификации " +
                        "\nError: " + e.getMessage());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllException(Exception e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("К сожалению, произошла серьозная ошибка. Обратитесь к админестратору! \nError: " + e.getMessage());
    }

}
