package com.weddingplanner.common.util;

import com.weddingplanner.common.exception.ValidationException;

/** Simple reusable input checks. Throws ValidationException with a friendly message. */
public final class Validator {

    private Validator() {
    }

    public static void requireNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " is required.");
        }
    }

    public static void requireEmail(String email) {
        requireNotBlank(email, "Email");
        if (!email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new ValidationException("Invalid email address.");
        }
    }

    /** Sri Lankan style: 10 digits, e.g. 0771234567 */
    public static void requirePhone(String phone) {
        requireNotBlank(phone, "Phone");
        if (!phone.matches("^0\\d{9}$")) {
            throw new ValidationException("Phone must be 10 digits starting with 0.");
        }
    }

    public static void requirePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException(fieldName + " must be greater than 0.");
        }
    }

    public static void requireNonNegative(double value, String fieldName) {
        if (value < 0) {
            throw new ValidationException(fieldName + " cannot be negative.");
        }
    }
}
