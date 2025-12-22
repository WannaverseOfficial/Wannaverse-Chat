package com.wannaverse.users.controllers;

import com.wannaverse.users.persistence.jpa.User;
import com.wannaverse.users.services.UserService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class CreateUserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserController.class);
    private final UserService service;
    private final Argon2PasswordEncoder passwordEncoder;

    @Autowired
    public CreateUserController(UserService service, Argon2PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        LOGGER.info("Received account creation request");

        if (service.isEmailAddressInUse(user.getEmailAddress())) {
            return ResponseEntity.badRequest().body("Email address is already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        service.save(user);

        LOGGER.info("{} has been created", user);

        return ResponseEntity.ok().build();
    }
}
