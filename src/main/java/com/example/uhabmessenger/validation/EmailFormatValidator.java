package com.example.uhabmessenger.validation;

import com.example.uhabmessenger.validation.annotation.EmailFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.example.uhabmessenger.formatutils.RegexUtils.EMAIL_REGEX;

public class EmailFormatValidator implements ConstraintValidator<EmailFormat, String> {

    @Override
    public void initialize(EmailFormat constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        return matchersEmailRegex(value);

    }

    public static boolean matchersEmailRegex(String value) {

        return value.matches(EMAIL_REGEX);

    }
}
