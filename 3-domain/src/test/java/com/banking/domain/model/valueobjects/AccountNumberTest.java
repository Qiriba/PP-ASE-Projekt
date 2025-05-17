package com.banking.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountNumberTest {

    @Test
    void validAccountNumberShouldBeAccepted() {
        String validNumber = "1234567890";
        AccountNumber accountNumber = new AccountNumber(validNumber);

        assertEquals(validNumber, accountNumber.getValue());
    }

    @Test
    void invalidAccountNumberShouldThrowException() {
        String tooShort = "12345";
        String letters = "12345abcde";
        String nullValue = null;

        assertThrows(IllegalArgumentException.class, () -> new AccountNumber(tooShort));
        assertThrows(IllegalArgumentException.class, () -> new AccountNumber(letters));
        assertThrows(IllegalArgumentException.class, () -> new AccountNumber(nullValue));
    }

    @Test
    void twoAccountNumbersWithSameValueShouldBeEqual() {
        AccountNumber one = new AccountNumber("1234567890");
        AccountNumber two = new AccountNumber("1234567890");

        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    @Test
    void accountNumberToStringShouldContainValue() {
        AccountNumber acc = new AccountNumber("1234567890");

        assertTrue(acc.toString().contains("1234567890"));
    }

    @Test
    void generatedAccountNumberShouldBeValid() {
        AccountNumber generated = AccountNumber.generate();

        assertNotNull(generated.getValue());
        assertTrue(generated.getValue().matches("\\d{10}"));
    }
}
