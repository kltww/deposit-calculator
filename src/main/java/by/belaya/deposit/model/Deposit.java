package by.belaya.deposit.model;

/**
 * Модель банковского вклада.
 *
 * Класс не зависит от Ant, Maven или Gradle.
 * Это принципиально: инструменты сборки должны
 * работать с одним и тем же исходным кодом.
 */
public class Deposit {
    private final double principal;      // Первоначальная сумма.
    private final double annualRate;     // Годовая ставка в процентах.
    private final int years;             // Срок вклада в годах.
    private final boolean capitalization; // Есть ли капитализация.

    public Deposit(
            double principal,
            double annualRate,
            int years,
            boolean capitalization) {
        if (principal <= 0) {
            throw new IllegalArgumentException(
                    "Сумма вклада должна быть > 0");
        }
        if (annualRate < 0) {
            throw new IllegalArgumentException(
                    "Ставка не может быть отрицательной");
        }
        if (years <= 0) {
            throw new IllegalArgumentException(
                    "Срок должен быть > 0");
        }
        this.principal = principal;
        this.annualRate = annualRate;
        this.years = years;
        this.capitalization = capitalization;
    }

    public double getPrincipal() {
        return principal;
    }

    public double getAnnualRate() {
        return annualRate;
    }

    public int getYears() {
        return years;
    }

    public boolean isCapitalization() {
        return capitalization;
    }
}
