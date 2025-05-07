package com.example.uhabmessenger.formatutils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class RegexUtils {

    /** Регулярное выражение для проверки:
    * - минимум 8 символов
     * - минимум 1 заглавная буква (A-Z)
     * - минимум 1 строчная буква (a-z)
     * - минимум 1 цифра (0-9)
     */
    public static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";

    /** Регулярное выражение для номера телефона
     * Поддерживает: +12345678901, +1 (123) 456-7890, +7 999 123-45-67, и т.д.
     */
    public final static String PHONE_REGEX = "^\\+?[1-9]\\d{0,2}(?:\\s?\\(\\d{1,4}\\)\\s?|\\s?\\d{1,4}\\s?|\\d{1,4})[-\\s]?\\d{1,4}[-\\s]?\\d{1,4}$";

    /** Регулярное выражение для email
     * Поддерживает: user@domain.com, user.name@sub.domain.co, и т.д.
     */
    public final static String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

}
