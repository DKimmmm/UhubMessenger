package com.example.uhabmessenger.dto.controllerAdviceException;

public record BadRequestExceptionDto(

        String title,
        String detail,
        Integer status

) {
}
