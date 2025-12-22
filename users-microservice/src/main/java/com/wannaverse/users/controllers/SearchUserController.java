package com.wannaverse.users.controllers;

import com.wannaverse.users.dto.SearchResults;
import com.wannaverse.users.persistence.jpa.User;
import com.wannaverse.users.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
            String query, Optional<Integer> page, Optional<Integer> size, Optional<Long> userId) {
        if (userId.isPresent()) {
            Optional<User> oUser = userService.getUserById(userId.get());

            if (oUser.isEmpty()) {
                return ResponseEntity.ok(new SearchResults<>(List.of(), 0, 0));
            }

            return ResponseEntity.ok(oUser.get().toElasticUser());
        }
        return ResponseEntity.ok(userService.search(query, page.orElse(0), size.orElse(10)));
    }
}
