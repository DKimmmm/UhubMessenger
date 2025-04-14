package com.example.UhabMessenger.authentication.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TitleCaseValidator implements ConstraintValidator<TitleCase, String> {

    @Override
    public void initialize(TitleCase constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return false; // Можно изменить на true, если null допустим
        }
        String trimmedValue = value.trim();
        if (trimmedValue.isEmpty()) {
            return false;
        }

        // Проверяем, что первая буква заглавная, а остальные строчные
        boolean isFirstUpper = Character.isUpperCase(trimmedValue.charAt(0));
        boolean isRestLower = trimmedValue.substring(1).equals(trimmedValue.substring(1).toLowerCase());

        return isFirstUpper && isRestLower;
    }
}
