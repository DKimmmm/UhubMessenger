package com.example.uhabmessenger.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TitleCaseValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TitleCase {
    String message() default "Format not tittle";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
