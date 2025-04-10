package com.banking.adapters.interfaces.controller;

import com.banking.application.ports.in.CreateUserUseCase;
import com.banking.domain.model.dto.CustomerRegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CreateUserUseCase createUserUseCase;


    public CustomerController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CustomerRegistrationRequest request) {
        createUserUseCase.register(request.username(), request.password(), request.pin());
        return ResponseEntity.ok("Customer registered");
    }
}