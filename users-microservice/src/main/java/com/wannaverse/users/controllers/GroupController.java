package com.wannaverse.users.controllers;

import com.wannaverse.users.persistence.UserGroup;
import com.wannaverse.users.services.UserGroupService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/group")
public class GroupController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);
    private final UserGroupService userGroupService;

    @Autowired
    public GroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @GetMapping("/")
    public ResponseEntity<UserGroup> getUserGroupById(long groupId) {
        return ResponseEntity.of(userGroupService.getUserGroupById(groupId));
    }

    @PostMapping("/")
    public ResponseEntity<?> createOrUpdateUserGroup(@Valid @RequestBody UserGroup userGroup) {
        LOGGER.trace("Received user group create/update request");

        userGroupService.saveUserGroup(userGroup);

        return ResponseEntity.ok().body("User group created.");
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteUserGroup(long groupId) {
        LOGGER.trace("Received user group removal request for {}", groupId);

        userGroupService.removeUserGroupById(groupId);

        return ResponseEntity.ok().build();
    }
}
