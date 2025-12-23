package com.wannaverse.users.controllers;

import com.wannaverse.users.persistence.User;
import com.wannaverse.users.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createOrUpdate(@RequestBody User user, Principal principal) {
        String userId = principal.getName();

        userService
                .getUserById(userId)
                .ifPresentOrElse(
                        u -> userService.updateUser(u, user),
                        () -> userService.createUser(userId, user));

        return ResponseEntity.ok().build();
    }
}
