package com.example.uhabmessenger.validation.method;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ImageOrTitleExistValidator.class)
@Target({METHOD, CONSTRUCTOR, ANNOTATION_TYPE, FIELD})
@Retention(value = RUNTIME)
public @interface ImageOrTitleExist {

    String message() default "valid method createPost format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
