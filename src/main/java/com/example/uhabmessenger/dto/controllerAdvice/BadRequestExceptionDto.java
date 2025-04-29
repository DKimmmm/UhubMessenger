package com.example.uhabmessenger.dto.controllerAdvice;

public record BadRequestExceptionDto(

        String title,
        String detail,
        Integer status

) {
}
