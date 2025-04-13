package com.banking.application.ports.in;

import com.banking.domain.model.Customer;

import java.util.Optional;

public interface LoginUseCase {
    String login(String username, String password);
}