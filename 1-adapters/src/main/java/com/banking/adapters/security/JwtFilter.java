package com.banking.adapters.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final static String SECRET_KEY = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        logger.info("JWT Filter invoked for request: " + request.getMethod() + " " + request.getRequestURI());
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        logger.info("Request URI: " + requestURI + ", Method: " + method);
        if ((requestURI.startsWith("/products") && "GET".equals(method)) ||
                requestURI.startsWith("/auth/") ||
                requestURI.startsWith("/customers/register") ||
                requestURI.equals("/categories")) {
            logger.debug("Request is to a public endpoint, skipping JWT validation.");
            chain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header: " + authorizationHeader);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Missing or Invalid JWT Token");
            return;
        }

        String token = authorizationHeader.substring(7);
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            logger.info("JWT successfully parsed. Subject: " + claims.getSubject());
        } catch (Exception e) {
            logger.warn("JWT parsing failed: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid JWT Token");
            return;
        }

        String username = claims.getSubject();
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("Setting authentication for user: " + username);
            User userDetails = new User(username, "", Collections.emptyList());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            logger.debug("Username is null or Authentication already set in context.");
        }

        chain.doFilter(request, response);
    }
}
