package com.example.UhabMessenger.userdata.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhoneOrEmailValidator implements ConstraintValidator<PhoneOrEmail, String> {

    private static final String PHONE_REGEX = "^\\+?[1-9]\\d{1,14}([\\s-]?\\d{2,4})*$|^\\+?[1-9]\\d{1,14}(\\(\\d{1,4}\\))?([\\s-]?\\d{2,4})*$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Logger log = LoggerFactory.getLogger(PhoneOrEmailValidator.class);

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


        // Валидно, если это телефон ИЛИ email
        boolean b = matchersEmailRegex(value) || matchersPhoneRegex(value);
        log.info("b = {}", b);
        return b;
    }

    public static boolean matchersPhoneRegex(String value) {
        return value.matches(PHONE_REGEX);
    }

    public static boolean matchersEmailRegex(String value) {
        return value.matches(EMAIL_REGEX);
    }
}
