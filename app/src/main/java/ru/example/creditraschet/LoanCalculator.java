package ru.example.creditraschet;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculates annuity loan payments and validates input ranges.
 */
public final class LoanCalculator {
    public static final long MIN_AMOUNT = 1_000L;
    public static final long MAX_AMOUNT = 10_000_000L;
    public static final double MIN_ANNUAL_RATE = 0.1;
    public static final double MAX_ANNUAL_RATE = 30.0;
    public static final int MIN_MONTHS = 1;
    public static final int MAX_MONTHS = 360;

    private LoanCalculator() {
    }

    public static LoanCalculationResult calculate(long amount, double annualRatePercent, int months) {
        validate(amount, annualRatePercent, months);

        double monthlyRate = annualRatePercent / 100.0 / 12.0;
        double coefficient = monthlyRate * Math.pow(1 + monthlyRate, months)
                / (Math.pow(1 + monthlyRate, months) - 1);
        double monthlyPayment = amount * coefficient;
        double totalPayment = monthlyPayment * months;
        double overpayment = totalPayment - amount;

        return new LoanCalculationResult(
                money(monthlyPayment),
                money(totalPayment),
                money(overpayment)
        );
    }

    public static void validate(long amount, double annualRatePercent, int months) {
        if (amount < MIN_AMOUNT || amount > MAX_AMOUNT) {
            throw new IllegalArgumentException("Сумма кредита должна быть от 1 000 до 10 000 000 ₽");
        }
        if (Double.isNaN(annualRatePercent)
                || annualRatePercent < MIN_ANNUAL_RATE
                || annualRatePercent > MAX_ANNUAL_RATE) {
            throw new IllegalArgumentException("Ставка должна быть от 0,1% до 30%");
        }
        if (months < MIN_MONTHS || months > MAX_MONTHS) {
            throw new IllegalArgumentException("Срок кредита должен быть от 1 до 360 месяцев");
        }
    }

    private static BigDecimal money(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}
