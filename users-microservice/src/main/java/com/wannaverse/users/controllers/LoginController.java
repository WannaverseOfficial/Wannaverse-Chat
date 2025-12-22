package com.wannaverse.users.controllers;

import com.wannaverse.users.persistence.User;
import com.wannaverse.users.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
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
