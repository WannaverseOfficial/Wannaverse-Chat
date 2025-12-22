package com.wannaverse.users.services;

import com.wannaverse.users.dto.SearchResults;
import com.wannaverse.users.dto.UserDTO;
import com.wannaverse.users.persistence.User;
import com.wannaverse.users.persistence.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Argon2PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, Argon2PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isEmailAddressInUse(String email) {
        return userRepository.findUserByEmailAddress(email).isPresent();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> getUserById(long id) {
        return userRepository.findUserById(id);
    }

    public Optional<User> getUserByEmail(String emailAddress) {
        return userRepository.findUserByEmailAddressIgnoreCase(emailAddress);
    }

    public SearchResults<UserDTO> search(String query, int page, int size) {
        Page<User> results =
                userRepository
                        .findByEmailAddressContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDisplayNameContainingIgnoreCase(
                                query, query, query, query, PageRequest.of(page, size));

        List<UserDTO> transformed =
                results.getContent().stream()
                        .map(
                                user ->
                                        new UserDTO(
                                                user.getId(),
                                                user.getFirstName(),
                                                user.getLastName(),
                                                user.getDisplayName()))
                        .toList();

        return new SearchResults<>(transformed, results.getNumber(), results.getTotalPages());
    }

    public boolean isPasswordCorrect(String attemptedPassword, String password) {
        return passwordEncoder.matches(attemptedPassword, password);
    }
}
