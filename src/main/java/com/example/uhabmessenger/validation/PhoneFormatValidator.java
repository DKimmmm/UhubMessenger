package com.example.uhabmessenger.validation;

import com.example.uhabmessenger.validation.annotation.PhoneFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.example.uhabmessenger.formatutils.RegexUtils.PHONE_REGEX;

public class PhoneFormatValidator implements ConstraintValidator<PhoneFormat, String> {

    @Override
    public void initialize(PhoneFormat constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        return matchersPhoneRegex(value);
    }

    public static boolean matchersPhoneRegex(String value) {
        return value.matches(PHONE_REGEX);
    }

}
