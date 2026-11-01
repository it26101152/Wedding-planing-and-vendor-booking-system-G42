package com.weddingplanner.integration;

import com.weddingplanner.common.util.ModuleValidation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationRegressionTests {
    @Test void validPaymentAmounts() {
        for (String amount : new String[]{"5000", "5000.00", "500000.00", "0.01", "999999999999.99", "1.000"})
            assertDoesNotThrow(() -> ModuleValidation.amount(amount, "Payment amount", false), amount);
    }
    @Test void invalidPaymentAmounts() {
        for (String amount : new String[]{"", "-1", "0", "5000.999", "500,000", "Rs. 5000", "1000000000000", "1.001"})
            assertThrows(IllegalArgumentException.class, () -> ModuleValidation.amount(amount, "Payment amount", false), amount);
    }
    @Test void validDatesAndStatuses() {
        assertDoesNotThrow(() -> ModuleValidation.date("2026-11-07", "Wedding date"));
        assertThrows(IllegalArgumentException.class, () -> ModuleValidation.date("2026-02-30", "Wedding date"));
        assertDoesNotThrow(() -> ModuleValidation.choice("Accepted", "RSVP", "Pending", "Accepted", "Declined"));
        assertThrows(IllegalArgumentException.class, () -> ModuleValidation.choice("Maybe", "RSVP", "Pending", "Accepted", "Declined"));
    }
}
