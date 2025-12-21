package com.wannaverse.users.controllers;

import com.wannaverse.users.persistence.User;
import com.wannaverse.users.services.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class LoginUserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginUserController.class);
    private static final String[] DEFAULT_SCOPES = {"user.read"};

    private final UserService service;
    private final Argon2PasswordEncoder passwordEncoder;

    @Autowired
    public LoginUserController(UserService service, Argon2PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public ResponseEntity<?> createUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String base64Header,
            Optional<String[]> scopes) {

        LOGGER.info("Received login request");

        String[] decoded = decodeBase64Header(base64Header);
        Optional<User> oUser = service.findUserByEmailAddress(decoded[0]);

        if (oUser.isEmpty() || !isUserPasswordCorrect(oUser.get(), decoded[1])) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        long userId = oUser.get().getId();
        String token = service.createJwtToken(userId, scopes.orElse(DEFAULT_SCOPES));
        String refresh = service.createRefreshToken(userId);

        return ResponseEntity.ok(new LoginUserResponse(token, refresh));
    }

    private boolean isUserPasswordCorrect(User user, String password) {
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    private static String[] decodeBase64Header(String base64) {
        String emailAddress = "";
        String password = "";
        try {
            String decoded =
                    new String(Base64.getDecoder().decode(base64.replace("Basic ", "").trim()));
            String[] split = decoded.split(":");
            emailAddress = split[0];
            password = split[1];
        } catch (Exception e) {
            LOGGER.error("Error while decoding auth header", e);
        }
        return new String[] {emailAddress, password};
    }
}
