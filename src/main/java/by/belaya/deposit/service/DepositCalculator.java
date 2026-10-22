package by.belaya.deposit.service;

import by.belaya.deposit.model.Deposit;

public class DepositCalculator {

    public double calculateSimple(Deposit deposit) {
        return deposit.getPrincipal()
                * (1
                + deposit.getAnnualRate()
                * deposit.getYears()
                / 100.0);
    }

    public double calculateCompound(Deposit deposit) {
        return deposit.getPrincipal()
                * Math.pow(
                        1 + deposit.getAnnualRate() / 100.0,
                        deposit.getYears());
    }

    public double calculateTotal(Deposit deposit) {
        return deposit.isCapitalization()
                ? calculateCompound(deposit)
                : calculateSimple(deposit);
    }

    public double calculateInterest(Deposit deposit) {
        return calculateTotal(deposit)
                - deposit.getPrincipal();
    }

    public String buildReport(Deposit deposit) {
        StringBuilder sb = new StringBuilder();
        sb.append("Отчет по вкладу\n");
        sb.append(String.format(
                "Начальная сумма: %.2f BYN%n",
                deposit.getPrincipal()));
        sb.append(String.format(
                "Ставка: %.2f%%%n",
                deposit.getAnnualRate()));
        sb.append(String.format(
                "Срок: %d лет%n",
                deposit.getYears()));
        sb.append(String.format(
                "Капитализация: %s%n",
                deposit.isCapitalization() ? "да" : "нет"));
        sb.append(String.format(
                "Итоговая сумма: %.2f BYN%n",
                calculateTotal(deposit)));
        sb.append(String.format(
                "Начислено процентов: %.2f BYN%n",
                calculateInterest(deposit)));
        return sb.toString();
    }
}
