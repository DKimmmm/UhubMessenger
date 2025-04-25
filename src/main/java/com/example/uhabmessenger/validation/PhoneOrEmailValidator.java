package com.example.uhabmessenger.validation;

import com.example.uhabmessenger.validation.annotation.PhoneOrEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.example.uhabmessenger.formatutils.RegexUtils.EMAIL_REGEX;
import static com.example.uhabmessenger.formatutils.RegexUtils.PHONE_REGEX;

public class PhoneOrEmailValidator implements ConstraintValidator<PhoneOrEmail, String> {

    @Override
    public void initialize(PhoneOrEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        return matchersEmailRegex(value) || matchersPhoneRegex(value);

    }

    public static boolean matchersPhoneRegex(String value) {

        return value.matches(PHONE_REGEX);

    }

    public static boolean matchersEmailRegex(String value) {

        return value.matches(EMAIL_REGEX);

    }
}
