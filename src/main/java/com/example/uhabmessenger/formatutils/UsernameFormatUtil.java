package com.example.uhabmessenger.formatutils;

import lombok.extern.slf4j.Slf4j;
import static com.example.uhabmessenger.formatutils.RegexUtils.EMAIL_REGEX;
import static com.example.uhabmessenger.formatutils.RegexUtils.PHONE_REGEX;

@Slf4j
public final class UsernameFormatUtil {

    private UsernameFormatUtil() {
    }

    /**
     * Проверка, соответствует ли username формату номера телефона
      */
    public static boolean usernameIsPhoneFormat(String username) {

        if (username == null || username.trim().isEmpty()) {
            log.debug("Username is null or empty, cannot validate as phone format");
            return false;
        }

        boolean isPhoneFormat = username.matches(PHONE_REGEX);
        log.debug("Checking if username '{}' is in phone format: {}", username, isPhoneFormat);
        return isPhoneFormat;

    }

    /**
     * Проверка, соответствует ли username формату email
      */
    public static boolean usernameIsEmailFormat(String username) {

        if (username == null || username.trim().isEmpty()) {
            log.debug("Username is null or empty, cannot validate as email format");
            return false;
        }

        boolean isEmailFormat = username.matches(EMAIL_REGEX);
        log.debug("Checking if username '{}' is in email format: {}", username, isEmailFormat);
        return isEmailFormat;

    }

}
