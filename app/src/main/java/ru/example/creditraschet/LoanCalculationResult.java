package ru.example.creditraschet;

import java.math.BigDecimal;

/**
 * Immutable result of an annuity loan calculation.
 */
public record LoanCalculationResult(
        BigDecimal monthlyPayment,
        BigDecimal totalPayment,
        BigDecimal overpayment
) {
}
