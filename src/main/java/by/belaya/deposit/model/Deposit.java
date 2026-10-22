package by.belaya.deposit.model;

public class Deposit {
    private final double principal;
    private final double annualRate;
    private final int years;
    private final boolean capitalization;

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
