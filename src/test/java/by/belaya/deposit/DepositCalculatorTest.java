package by.belaya.deposit;

import by.belaya.deposit.model.Deposit;
import by.belaya.deposit.service.DepositCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульные тесты бизнес-логики.
 * Группы: позитивные (simple, compound, total, interest),
 * граничные (zeroRate, oneYear) и ошибочные (exceptions) + отчет.
 */
@DisplayName("Тесты калькулятора вкладов")
class DepositCalculatorTest {

    private DepositCalculator calculator;

    @BeforeEach
    void setUp() {
        // Каждый тест получает новый экземпляр сервиса.
        calculator = new DepositCalculator();
    }

    @Test
    @DisplayName("Простые проценты: 1000 BYN, 10%, 3 года = 1300")
    void simpleInterest() {
        Deposit deposit =
                new Deposit(1000, 10, 3, false);
        assertEquals(
                1300.0,
                calculator.calculateSimple(deposit),
                0.001);
    }

    @Test
    @DisplayName("Сложные проценты: 1000 BYN, 10%, 3 года = 1331")
    void compoundInterest() {
        Deposit deposit =
                new Deposit(1000, 10, 3, true);
        assertEquals(
                1331.0,
                calculator.calculateCompound(deposit),
                0.001);
    }

    @Test
    @DisplayName("calculateTotal выбирает режим капитализации")
    void totalDependsOnCapitalization() {
        Deposit simple =
                new Deposit(1000, 10, 3, false);
        Deposit compound =
                new Deposit(1000, 10, 3, true);
        assertEquals(
                1300.0,
                calculator.calculateTotal(simple),
                0.001);
        assertEquals(
                1331.0,
                calculator.calculateTotal(compound),
                0.001);
    }

    @Test
    @DisplayName("Начисленные проценты положительны")
    void interestIsPositive() {
        Deposit deposit =
                new Deposit(1000, 10, 3, true);
        assertTrue(
                calculator.calculateInterest(deposit) > 0);
    }

    @Test
    @DisplayName("Отрицательная сумма вызывает исключение")
    void negativePrincipalIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Deposit(-100, 10, 3, false));
    }

    @Test
    @DisplayName("Нулевой срок вызывает исключение")
    void zeroYearsIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Deposit(1000, 10, 0, false));
    }

    @Test
    @DisplayName("Отрицательная ставка вызывает исключение")
    void negativeRateIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Deposit(1000, -1, 3, false));
    }

    @Test
    @DisplayName("Нулевая ставка не изменяет сумму")
    void zeroRateDoesNotChangePrincipal() {
        Deposit deposit =
                new Deposit(1000, 0, 5, false);
        assertEquals(
                1000.0,
                calculator.calculateTotal(deposit),
                0.001);
    }

    @Test
    @DisplayName("Отчет содержит ключевые поля")
    void reportContainsKeyFields() {
        Deposit deposit =
                new Deposit(1000, 10, 3, true);
        String report =
                calculator.buildReport(deposit);
        assertAll(
                () -> assertTrue(
                        report.contains("Итоговая сумма")),
                () -> assertTrue(
                        report.contains("Начислено процентов")),
                () -> assertTrue(
                        report.contains("Капитализация"))
        );
    }
}
