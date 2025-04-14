package com.example.UhabMessenger.authentication.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneOrEmailValidator implements ConstraintValidator<PhoneOrEmail, String> {

    private static final String PHONE_REGEX = "^\\+?[1-9]\\d{1,14}([\\s-]?\\d{2,4})*$|^\\+?[1-9]\\d{1,14}(\\(\\d{1,4}\\))?([\\s-]?\\d{2,4})*$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Override
    public void initialize(PhoneOrEmail constraintAnnotation) {
        // Инициализация, если нужно
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Проверка на null или пустую строку
        if (value == null || value.trim().isEmpty()) {
            return false; // Можно настроить под ваши требования
        }

        // Проверка на соответствие формату телефона или email
        boolean isPhone = value.matches(PHONE_REGEX);
        boolean isEmail = value.matches(EMAIL_REGEX);

        // Валидно, если это телефон ИЛИ email
        return isPhone || isEmail;
    }
}
