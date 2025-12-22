package com.wannaverse.users.services;

import com.wannaverse.users.persistence.UserGroup;
import com.wannaverse.users.persistence.UserGroupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;

    @Autowired
    public UserGroupService(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    public Optional<UserGroup> getUserGroupById(long id) {
        return this.userGroupRepository.findUserGroupById(id);
    }

    public void saveUserGroup(UserGroup userGroup) {
        userGroupRepository.save(userGroup);
    }

    public void removeUserGroupById(long userGroupId) {
        userGroupRepository
                .findUserGroupById(userGroupId)
                .ifPresent(u -> userGroupRepository.save(u));
    }
}
