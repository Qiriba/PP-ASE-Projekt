package com.banking.adapters.security;


import com.banking.domain.model.Customer;
import com.banking.domain.security.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import java.util.*;
@Component
public class JwtUtil implements JwtProvider {

    private final static SecretKey SECRET_KEY = Keys.hmacShaKeyFor("MySuperSecureSecretKeyThatIsLongEnoughForHS256".getBytes(StandardCharsets.UTF_8));

    @Override
    public String generateToken(Customer customer) {
        return Jwts.builder()
                .setSubject(customer.getUsername())
                .claim("customerId", customer.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET_KEY)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public UUID extractCustomerId(String token) {
        return UUID.fromString(extractClaim(token, claims -> claims.get("customerId", String.class)));
    }
    @Override
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}