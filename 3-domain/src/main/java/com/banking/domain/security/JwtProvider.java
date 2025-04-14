package com.banking.domain.security;

import com.banking.domain.model.Customer;

public interface JwtProvider {
    String generateToken(Customer customer);
    String extractUsername(String token);
    boolean validateToken(String token, String username);
}