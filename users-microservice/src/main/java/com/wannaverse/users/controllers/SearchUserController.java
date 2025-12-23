package com.wannaverse.users.controllers;

import com.wannaverse.users.persistence.User;
import com.wannaverse.users.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class SearchUserController {

    private final UserService userService;

    @Autowired
    public SearchUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            String query, Optional<Integer> page, Optional<Integer> size, Optional<String> userId) {

        if (userId.isEmpty()) {
            return ResponseEntity.ok(userService.search(query, page.orElse(0), size.orElse(10)));
        }

        Optional<User> optionalUser = userService.getUserById(userId.get());

        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        }

        return ResponseEntity.ok().build();
    }
}
