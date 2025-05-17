package com.banking.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void validMoneyShouldBeAccepted() {
        BigDecimal amount = new BigDecimal("100.00");
        Currency currency = Currency.getInstance("EUR");
        Money money = new Money(amount, currency);

        assertEquals(amount.setScale(2), money.getAmount());
        assertEquals(currency, money.getCurrency());
    }

    @Test
    void nullAmountOrCurrencyShouldThrowException() {
        Currency currency = Currency.getInstance("EUR");
        BigDecimal amount = new BigDecimal("100.00");

        assertThrows(IllegalArgumentException.class, () -> new Money(null, currency));
        assertThrows(IllegalArgumentException.class, () -> new Money(amount, null));
    }

    @Test
    void amountWithMoreThanTwoDecimalPlacesShouldThrowException() {
        Currency currency = Currency.getInstance("EUR");
        BigDecimal amount = new BigDecimal("100.123");

        assertThrows(IllegalArgumentException.class, () -> new Money(amount, currency));
    }

    @Test
    void negativeAmountShouldThrowException() {
        Currency currency = Currency.getInstance("EUR");
        BigDecimal amount = new BigDecimal("-1.00");

        assertThrows(IllegalArgumentException.class, () -> new Money(amount, currency));
    }

    @Test
    void twoMoneyObjectsWithSameAmountAndCurrencyShouldBeEqual() {
        Money money1 = new Money(new BigDecimal("50.00"), Currency.getInstance("USD"));
        Money money2 = new Money(new BigDecimal("50.00"), Currency.getInstance("USD"));

        assertEquals(money1, money2);
        assertEquals(money1.hashCode(), money2.hashCode());
    }

    @Test
    void toStringShouldContainAmountAndCurrencyCode() {
        Money money = new Money(new BigDecimal("75.50"), Currency.getInstance("EUR"));
        String result = money.toString();

        assertTrue(result.contains("75.50"));
        assertTrue(result.contains("EUR"));
    }

    @Test
    void addShouldReturnCorrectSumWithSameCurrency() {
        Money one = new Money(new BigDecimal("20.00"), Currency.getInstance("EUR"));
        Money two = new Money(new BigDecimal("30.00"), Currency.getInstance("EUR"));

        Money result = one.add(two);

        assertEquals(new BigDecimal("50.00"), result.getAmount());
        assertEquals(Currency.getInstance("EUR"), result.getCurrency());
    }

    @Test
    void addShouldThrowExceptionForDifferentCurrencies() {
        Money one = new Money(new BigDecimal("20.00"), Currency.getInstance("EUR"));
        Money two = new Money(new BigDecimal("30.00"), Currency.getInstance("USD"));

        assertThrows(IllegalArgumentException.class, () -> one.add(two));
    }

    @Test
    void subtractShouldReturnCorrectDifferenceWithSameCurrency() {
        Money one = new Money(new BigDecimal("50.00"), Currency.getInstance("EUR"));
        Money two = new Money(new BigDecimal("20.00"), Currency.getInstance("EUR"));

        Money result = one.subtract(two);

        assertEquals(new BigDecimal("30.00"), result.getAmount());
        assertEquals(Currency.getInstance("EUR"), result.getCurrency());
    }

    @Test
    void subtractShouldThrowExceptionForDifferentCurrencies() {
        Money one = new Money(new BigDecimal("50.00"), Currency.getInstance("EUR"));
        Money two = new Money(new BigDecimal("20.00"), Currency.getInstance("USD"));

        assertThrows(IllegalArgumentException.class, () -> one.subtract(two));
    }

    @Test
    void isGreaterOrEqualShouldWorkCorrectly() {
        Money one = new Money(new BigDecimal("50.00"), Currency.getInstance("EUR"));
        Money two = new Money(new BigDecimal("20.00"), Currency.getInstance("EUR"));
        Money three = new Money(new BigDecimal("50.00"), Currency.getInstance("EUR"));
        Money four = new Money(new BigDecimal("60.00"), Currency.getInstance("EUR"));

        assertTrue(one.isGreaterOrEqual(two));
        assertTrue(one.isGreaterOrEqual(three));
        assertFalse(one.isGreaterOrEqual(four));
    }

    @Test
    void isGreaterOrEqualShouldThrowExceptionForDifferentCurrencies() {
        Money one = new Money(new BigDecimal("50.00"), Currency.getInstance("EUR"));
        Money two = new Money(new BigDecimal("20.00"), Currency.getInstance("USD"));

        assertThrows(IllegalArgumentException.class, () -> one.isGreaterOrEqual(two));
    }

    @Test
    void staticFactoryMethodsShouldCreateValidMoneyObjects() {
        Money moneyFromStrings = Money.of("100.00", "EUR");
        assertEquals(new BigDecimal("100.00").setScale(2), moneyFromStrings.getAmount());
        assertEquals(Currency.getInstance("EUR"), moneyFromStrings.getCurrency());

        Money moneyFromBigDecimal = Money.of(new BigDecimal("150.00"));
        assertEquals(new BigDecimal("150.00").setScale(2), moneyFromBigDecimal.getAmount());
        assertEquals(Currency.getInstance("EUR"), moneyFromBigDecimal.getCurrency());
    }
}
