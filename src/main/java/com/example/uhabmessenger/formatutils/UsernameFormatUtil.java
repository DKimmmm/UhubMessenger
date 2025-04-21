package com.example.uhabmessenger.formatutils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsernameFormatUtil {

    /** Регулярное выражение для номера телефона
    * Поддерживает: +12345678901, +1 (123) 456-7890, +7 999 123-45-67, и т.д.
     */
    private final static String phoneRegex = "^\\+?[1-9]\\d{1,14}([\\s-]?\\d{2,4})*$|^\\+?[1-9]\\d{1,14}(\\(\\d{1,4}\\))?([\\s-]?\\d{2,4})*$";

    /** Регулярное выражение для email
    * Поддерживает: user@domain.com, user.name@sub.domain.co, и т.д.
    */
    private final static String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private UsernameFormatUtil() {
    }

    /**
     * Проверка, соответствует ли username формату номера телефона
      */
    public static boolean usernameIsPhoneFormat(String username) {
        if (username == null || username.trim().isEmpty()) {
            log.info("Username is null or empty, cannot validate as phone format");
            return false;
        }

        boolean isPhoneFormat = username.matches(phoneRegex);
        log.info("Checking if username '{}' is in phone format: {}", username, isPhoneFormat);
        return isPhoneFormat;
    }

    /**
     * Проверка, соответствует ли username формату email
      */
    public static boolean usernameIsEmailFormat(String username) {
        if (username == null || username.trim().isEmpty()) {
            log.info("Username is null or empty, cannot validate as email format");
            return false;
        }

        boolean isEmailFormat = username.matches(emailRegex);
        log.info("Checking if username '{}' is in email format: {}", username, isEmailFormat);
        return isEmailFormat;
    }

}
