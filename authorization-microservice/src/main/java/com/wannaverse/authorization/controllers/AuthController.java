package com.wannaverse.authorization.controllers;

import com.wannaverse.authorization.persistence.User;
import com.wannaverse.authorization.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/oauth2")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final Argon2PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, Argon2PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        LOGGER.info("Received account creation request");

        if (userService.isEmailAddressInUse(user.getEmailAddress())) {
            return ResponseEntity.badRequest().body("Email address is already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);

        LOGGER.info("{} has been created", user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            HttpServletRequest request, @RequestBody LoginRequest loginRequest) {
        Optional<User> optionalUser = userService.getUserByEmail(loginRequest.getUsername());

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        User user = optionalUser.get();

        if (!userService.isPasswordCorrect(loginRequest.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        user.getId(), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);

        request.getSession(true)
                .setAttribute(
                        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(Optional<String> code) {
        if (code.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(code);
    }
}
