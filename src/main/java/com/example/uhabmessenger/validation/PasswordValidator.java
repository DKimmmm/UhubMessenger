package com.example.uhabmessenger.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.example.uhabmessenger.formatutils.RegexUtils.PASSWORD_REGEX;

public class PasswordValidator implements ConstraintValidator<Password, String> {


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
