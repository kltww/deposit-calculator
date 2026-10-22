package by.belaya.deposit;

import by.belaya.deposit.model.Deposit;
import by.belaya.deposit.service.DepositCalculator;

public class Main {
    public static void main(String[] args) {
        Deposit deposit = new Deposit(1000, 10, 3, true);
        DepositCalculator calculator = new DepositCalculator();
        System.out.println(
                calculator.buildReport(deposit));
    }
}
