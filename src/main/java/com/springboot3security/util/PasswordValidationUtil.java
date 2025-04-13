package com.springboot3security.util;

public class PasswordValidationUtil {

    public static boolean isValidPassword(String password) {
        // Check if the password is at least 8 characters long
        if (password.length() < 8) {
            return false;
        }

        // Check if the password contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        // Check if the password contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return false;
        }

        // Check if the password contains at least one digit
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // TODO - Check if the password contains at least one special character should be added in production

        return true;
    }

    public static String getPasswordRequirements() {
        return "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one number.";
    }
}
