package ru.example.creditraschet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.math.BigDecimal;

import org.junit.Test;

public class LoanCalculatorTest {
    @Test
    public void calculatesAnnuityPaymentForSampleValues() {
        LoanCalculationResult result = LoanCalculator.calculate(100_000, 15.0, 12);

        assertEquals(new BigDecimal("9025.83"), result.monthlyPayment());
        assertEquals(new BigDecimal("108309.97"), result.totalPayment());
        assertEquals(new BigDecimal("8309.97"), result.overpayment());
    }

    @Test
    public void rejectsInvalidAmountRateAndTerm() {
        assertThrows(IllegalArgumentException.class, () -> LoanCalculator.calculate(999, 15.0, 12));
        assertThrows(IllegalArgumentException.class, () -> LoanCalculator.calculate(100_000, 0.09, 12));
        assertThrows(IllegalArgumentException.class, () -> LoanCalculator.calculate(100_000, 15.0, 361));
    }
}
