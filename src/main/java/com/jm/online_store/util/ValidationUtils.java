package com.jm.online_store.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ValidationUtils {

    public final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9@#$%]).{8,}", Pattern.CASE_INSENSITIVE);

    public final Pattern VALID_TELEPHONE_NUMBER_REGEX =
            Pattern.compile("(?:\\+7)[\\d\\(\\) ]{15,}\\d", Pattern.CASE_INSENSITIVE);

    public boolean isValidPassword(String password) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
        return matcher.matches();
    }

    public boolean isValidEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();
    }

    public boolean isNotValidEmail(String email) {
        return !isValidEmail(email);
    }

    public boolean isValidTelephoneNumber(String telephoneNumber) {
        Matcher matcher = VALID_TELEPHONE_NUMBER_REGEX.matcher(telephoneNumber);
        return matcher.matches();
    }
}