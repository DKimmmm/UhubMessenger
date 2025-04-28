package com.example.uhabmessenger.validation;

import com.example.uhabmessenger.validation.annotation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.example.uhabmessenger.formatutils.RegexUtils.PASSWORD_REGEX;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password.isBlank()) {
            return false;
        }
        return password.matches(PASSWORD_REGEX);
    }

}
