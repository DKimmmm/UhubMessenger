package com.example.uhabmessenger.validation.fieldFormat;

import com.example.uhabmessenger.validation.fieldFormat.annotation.PhoneFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.example.uhabmessenger.formatutils.RegexUtils.PHONE_REGEX;

public class PhoneFormatValidator implements ConstraintValidator<PhoneFormat, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value.isBlank()) {
            return true;
        }

        return matchersPhoneRegex(value);
    }

    public static boolean matchersPhoneRegex(String value) {
        return value.matches(PHONE_REGEX);
    }

}
