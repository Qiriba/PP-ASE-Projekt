package com.banking.domain.security;

public interface JwtProvider {
    String generateToken(String username);
    String extractUsername(String token);
    boolean validateToken(String token, String username);
}