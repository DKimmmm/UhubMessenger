package com.example.uhabmessenger.controller.advice;

import com.example.uhabmessenger.dto.controllerAdvice.BadRequestExceptionDto;
import com.example.uhabmessenger.exception.AuthorizationException;
import com.example.uhabmessenger.exception.DownloadImageException;
import com.example.uhabmessenger.exception.GroupSaveException;
import com.example.uhabmessenger.exception.ImageSaveException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
@RestControllerAdvice
public class UhubRestControllerAdvice {

    @ExceptionHandler(value = AuthorizationException.class)
    public ResponseEntity<BadRequestExceptionDto> handleAuthorizationException(Exception e) {

        e.printStackTrace();

        return new ResponseEntity<>(new BadRequestExceptionDto
                ("К сожалению, произошла ошибка во время авторизации.", e.getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<BadRequestExceptionDto> handleEntityNotFoundException(Exception e) {

        e.printStackTrace();

        return new ResponseEntity<>(new BadRequestExceptionDto
                ("Искомый ресурс не найден.", e.getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = {GroupSaveException.class, ImageSaveException.class, DownloadImageException.class})
    public ResponseEntity<BadRequestExceptionDto> handleOtherException(Exception e) {

        e.printStackTrace();

        return new ResponseEntity<>(new BadRequestExceptionDto
                ("К сожалению, произошла ошибка.", e.getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, IllegalArgumentException.class,
            MissingServletRequestPartException.class, MissingPathVariableException.class})
    public ResponseEntity<BadRequestExceptionDto> handleNoValidDataException(Exception e) {

        e.printStackTrace();

        return new ResponseEntity<>(new BadRequestExceptionDto
                ("Получены данные некорректного формата, попробуйте снова.", e.getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<BadRequestExceptionDto> handleHttpClientException(HttpClientErrorException e) {

        e.printStackTrace();

        return new ResponseEntity<>(new BadRequestExceptionDto
                ("Доступ закрыт. Либо ограничен доступ, либо не предоставлены данные для аутентификации ", e.getMessage(), HttpStatus.valueOf(403).value()),
                HttpStatus.valueOf(403));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BadRequestExceptionDto> handleAllException(Exception e) {

        e.printStackTrace();

        return new ResponseEntity<>(new BadRequestExceptionDto
                ("К сожалению, произошла серьезная ошибка. Обратитесь к администратору! ", e.getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);

    }

}
