package com.banking.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public class Password {

    private final String password;
    protected Password() {
        this.password = null;
    }
    public Password(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Passwort muss mindestens 6 Zeichen lang sein.");
        }
        this.password = password;
    }

    public boolean matches(String otherPassword) {
        return this.password.equals(otherPassword);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password that)) return false;
        return password.equals(that.password);
    }

    @JsonValue
    public String getPassword() {
        return password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
