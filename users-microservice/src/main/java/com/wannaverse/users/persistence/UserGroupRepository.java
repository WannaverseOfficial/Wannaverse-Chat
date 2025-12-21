package com.wannaverse.users.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, String> {
    Optional<UserGroup> findUserGroupById(String id);
}
