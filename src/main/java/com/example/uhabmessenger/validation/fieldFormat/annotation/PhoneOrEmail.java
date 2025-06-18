package com.example.uhabmessenger.validation.fieldFormat.annotation;

import com.example.uhabmessenger.validation.fieldFormat.PhoneOrEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneOrEmailValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneOrEmail {
    String message() default "valid phone or email format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}