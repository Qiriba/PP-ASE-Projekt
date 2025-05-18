package com.banking.adapters.interfaces.controller;

import com.banking.adapters.security.JwtUtil;
import com.banking.application.ports.in.AccountCreationUseCase;
import com.banking.application.services.CustomerAuthService;
import com.banking.domain.model.Account;
import com.banking.domain.model.Customer;
import com.banking.domain.model.dto.AccountCreationRequestDTO;
import com.banking.adapters.persistence.CustomerRepository;
import com.banking.domain.model.exceptions.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountCreatorController {

    private final AccountCreationUseCase accountCreationUseCase;
    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;
    private final CustomerAuthService customerAuthService;
    public AccountCreatorController(AccountCreationUseCase accountCreationUseCase, CustomerRepository customerRepository, JwtUtil jwtUtil, CustomerAuthService customerAuthService) {
        this.accountCreationUseCase = accountCreationUseCase;
        this.customerRepository = customerRepository;
        this.jwtUtil = jwtUtil;
        this.customerAuthService = customerAuthService;
    }

    @PostMapping
    public ResponseEntity<UUID> createAccount(@RequestBody AccountCreationRequestDTO requestDTO,
                                                 @RequestHeader("Authorization") String authHeader) {

        Customer customer = customerAuthService.getCustomerFromToken(authHeader);
        Account account = accountCreationUseCase.createAccount(customer.getId(), requestDTO);
        return new ResponseEntity<>(account.getId(), HttpStatus.CREATED);
    }
}
