package com.wannaverse.authorization.services;

import com.wannaverse.authorization.persistence.User;
import com.wannaverse.authorization.persistence.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

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
        return userRepository.findUserByEmailAddressIgnoreCase(email).isPresent();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String emailAddress) {
        return userRepository.findUserByEmailAddressIgnoreCase(emailAddress);
    }

    public boolean isPasswordCorrect(String attemptedPassword, String password) {
        return passwordEncoder.matches(attemptedPassword, password);
    }
}
