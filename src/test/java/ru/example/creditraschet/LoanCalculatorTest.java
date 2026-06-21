package ru.example.creditraschet;

import java.math.BigDecimal;

class LoanCalculatorTest {
    public static void main(String[] args) {
        calculatesAnnuityPaymentForSampleValues();
        rejectsInvalidAmountRateAndTerm();
        System.out.println("All loan calculator tests passed");
    }

    private static void calculatesAnnuityPaymentForSampleValues() {
        LoanCalculationResult result = LoanCalculator.calculate(100_000, 15.0, 12);

        assertEquals(new BigDecimal("9025.83"), result.monthlyPayment());
        assertEquals(new BigDecimal("108309.97"), result.totalPayment());
        assertEquals(new BigDecimal("8309.97"), result.overpayment());
    }

    private static void rejectsInvalidAmountRateAndTerm() {
        assertThrows(() -> LoanCalculator.calculate(999, 15.0, 12));
        assertThrows(() -> LoanCalculator.calculate(100_000, 0.09, 12));
        assertThrows(() -> LoanCalculator.calculate(100_000, 15.0, 361));
    }

    private static void assertEquals(BigDecimal expected, BigDecimal actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    private static void assertThrows(Runnable runnable) {
        try {
            runnable.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Expected IllegalArgumentException");
    }
}
