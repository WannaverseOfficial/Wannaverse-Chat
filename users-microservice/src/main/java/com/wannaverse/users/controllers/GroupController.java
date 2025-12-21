package com.wannaverse.users.controllers;

import com.wannaverse.users.persistence.UserGroup;
import com.wannaverse.users.services.UserGroupService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
public class GroupController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);
    private final UserGroupService userGroupService;

    @Autowired
    public GroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @GetMapping("/")
    public ResponseEntity<UserGroup> getUserGroupById(@Valid @RequestBody String id) {
        return ResponseEntity.of(userGroupService.getUserGroupById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUserGroup(@Valid @RequestBody UserGroup userGroup) {
        LOGGER.trace("Received user group creation request");

        userGroupService.createUserGroup(userGroup);

        return ResponseEntity.ok().body("User group created.");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> deleteUserGroup(@Valid @RequestBody String userGroupId) {
        LOGGER.trace("Received user group removal request");

        if (!userGroupService.removeUserGroupById(userGroupId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body("User group removed.");
    }
}
