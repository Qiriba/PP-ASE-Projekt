package com.banking.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public class PIN {

    private final String pin;
    protected PIN() {
        this.pin = null;
    }
    public PIN(String pin) {
        if (pin == null || pin.length() != 4) {
            throw new IllegalArgumentException("PIN muss genau 4 Zeichen lang sein.");
        }
        this.pin = pin;
    }

    public boolean matches(String otherPin) {
        return this.pin.equals(otherPin);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PIN pin1)) return false;
        return pin.equals(pin1.pin);
    }

    @JsonValue
    public String getPIN() {
        return pin;
    }
    @Override
    public int hashCode() {
        return Objects.hash(pin);
    }
}
