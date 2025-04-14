package com.example.UhabMessenger.authentication.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    // Регулярное выражение для проверки:
    // - минимум 8 символов
    // - минимум 1 заглавная буква (A-Z)
    // - минимум 1 строчная буква (a-z)
    // - минимум 1 цифра (0-9)
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";
    private static final Logger log = LoggerFactory.getLogger(PasswordValidator.class);

    @Override
    public void initialize(Password constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        // Проверка на null или пустую строку
        if (password == null || password.trim().isEmpty()) {
            return false; // Можно настроить под ваши требования
        }

        // Проверка пароля на соответствие требованиям
        boolean matches = password.matches(PASSWORD_REGEX);
        log.info("password: {}, answer: {}", password, matches);
//        return false;
        return matches;
    }
}
