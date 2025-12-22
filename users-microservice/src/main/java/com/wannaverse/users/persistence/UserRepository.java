package com.wannaverse.users.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmailAddress(String emailAddress);

    Optional<User> findUserById(long id);

    Page<User>
            findByEmailAddressContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDisplayNameContainingIgnoreCase(
                    String emailAddress,
                    String firstName,
                    String lastName,
                    String displayName,
                    Pageable pageable);
}
