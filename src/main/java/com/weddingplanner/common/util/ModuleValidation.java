package com.weddingplanner.common.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Shared server-side validation for the five project modules. */
public final class ModuleValidation {
    private ModuleValidation() {}
    public static String required(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " is required");
        String trimmed = value.trim();
        if (trimmed.length() > 250) throw new IllegalArgumentException(label + " is too long");
        return trimmed;
    }
    public static void date(String value, String label) {
        String text = required(value, label);
        try { LocalDate.parse(text); }
        catch (DateTimeParseException ex) { throw new IllegalArgumentException(label + " must be a valid date (YYYY-MM-DD)"); }
    }
    public static void amount(String value, String label, boolean allowZero) {
        try {
            BigDecimal n = new BigDecimal(required(value, label));
            if ((allowZero && n.signum() < 0) || (!allowZero && n.signum() <= 0))
                throw new IllegalArgumentException(label + " must be " + (allowZero ? "zero or greater" : "greater than zero"));
            // Decimal scale/precision alone is misleading for values such as 1000.00.
            // Accept up to 12 digits before the decimal point and at most 2 after it.
            BigDecimal normalized = n.stripTrailingZeros();
            int fractionalDigits = Math.max(0, normalized.scale());
            int integerDigits = Math.max(1, normalized.precision() - normalized.scale());
            if (fractionalDigits > 2 || integerDigits > 12)
                throw new IllegalArgumentException(label + " must have at most 12 integer digits and 2 decimal places");
        } catch (NumberFormatException ex) { throw new IllegalArgumentException(label + " must be a valid number"); }
    }
    public static void email(String value) {
        String text = required(value, "Guest email");
        if (!text.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) throw new IllegalArgumentException("Enter a valid guest email address");
    }
    public static void choice(String value, String label, String... allowed) {
        String text = required(value, label);
        for (String option : allowed) if (option.equalsIgnoreCase(text)) return;
        throw new IllegalArgumentException("Select a valid " + label.toLowerCase());
    }
}
