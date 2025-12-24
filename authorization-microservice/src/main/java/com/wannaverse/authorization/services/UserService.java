package com.wannaverse.authorization.services;

import com.wannaverse.authorization.persistence.User;
import com.wannaverse.authorization.persistence.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Argon2PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public UserService(
            UserRepository userRepository,
            Argon2PasswordEncoder passwordEncoder,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kafkaTemplate = kafkaTemplate;
    }

    public boolean isEmailAddressInUse(String email) {
        return userRepository.findUserByEmailAddressIgnoreCase(email).isPresent();
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);

        kafkaTemplate.send("new-user-registration", user.getId());
    }

    public Optional<User> getUserByEmail(String emailAddress) {
        return userRepository.findUserByEmailAddressIgnoreCase(emailAddress);
    }

    public boolean isPasswordCorrect(String attemptedPassword, String password) {
        return passwordEncoder.matches(attemptedPassword, password);
    }
}
