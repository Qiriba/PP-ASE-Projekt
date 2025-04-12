package com.banking.domain.model.valueobjects;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;


public class Money {

    private final BigDecimal amount;
    private final Currency currency;
    protected Money() {
        this.amount = null;
        this.currency = null;
    }
    public Money(BigDecimal amount, Currency currency) {
        if (amount == null || currency == null) throw new IllegalArgumentException("Nullwerte nicht erlaubt");
        if (amount.scale() > 2) throw new IllegalArgumentException("Nur 2 Nachkommastellen erlaubt");
        if (amount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Negative Beträge nicht erlaubt");

        this.amount = amount.setScale(2);
        this.currency = currency;
    }

    public Money add(Money other) {
        ensureSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        ensureSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public boolean isGreaterOrEqual(Money other) {
        ensureSameCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }

    private void ensureSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Währungen müssen übereinstimmen");
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return amount.equals(money.amount) && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency.getCurrencyCode();
    }

    public static Money of(String amount, String currencyCode) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currencyCode));
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount, Currency.getInstance("EUR")); // Standardwährung EUR
    }
}
