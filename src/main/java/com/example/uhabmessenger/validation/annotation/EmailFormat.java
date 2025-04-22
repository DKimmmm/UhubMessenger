package com.example.uhabmessenger.validation.annotation;

import com.example.uhabmessenger.validation.EmailFormatValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailFormatValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailFormat {
    String message() default "valid phone format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}