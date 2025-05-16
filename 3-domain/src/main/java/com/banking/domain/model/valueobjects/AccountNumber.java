package com.banking.domain.model.valueobjects;

import java.util.Objects;
import java.util.UUID;

public class AccountNumber {

    private final String value;

    public AccountNumber(String value) {
        System.out.println("AccountNumber created with value: " + value);
        if (value == null || !value.matches("\\d{10}")) {
            throw new IllegalArgumentException("Invalid account number format. Must be 10 digits.");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountNumber)) return false;
        AccountNumber that = (AccountNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "AccountNumber{" + "value='" + value + '\'' + '}';
    }

    public static AccountNumber generate() {
        long number = (long)(Math.random() * 1_000_000_0000L);
        String formattedNumber = String.format("%010d", number);
        return new AccountNumber(formattedNumber);
    }
}