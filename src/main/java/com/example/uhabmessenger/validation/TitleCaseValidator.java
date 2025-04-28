package com.example.uhabmessenger.validation;

import com.example.uhabmessenger.validation.annotation.TitleCase;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TitleCaseValidator implements ConstraintValidator<TitleCase, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value.isBlank()) {
            return true;
        }

        String trimmedValue = value.trim();

        boolean isFirstUpper = Character.isUpperCase(trimmedValue.charAt(0));
        boolean isRestLower = trimmedValue.substring(1).equals(trimmedValue.substring(1).toLowerCase());

        return isFirstUpper && isRestLower;
    }
}
