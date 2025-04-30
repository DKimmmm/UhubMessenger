package com.example.uhabmessenger.validation.fieldFormat.annotation;

import com.example.uhabmessenger.validation.fieldFormat.PhoneFormatValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneFormatValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneFormat {
    String message() default "valid phone format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}