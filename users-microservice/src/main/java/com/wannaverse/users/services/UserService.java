package com.wannaverse.users.services;

import com.wannaverse.users.jwt.Jwt;
import com.wannaverse.users.persistence.User;
import com.wannaverse.users.persistence.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isEmailAddressInUse(String email) {
        return userRepository.findUserByEmailAddress(email).isPresent();
    }

    public Optional<User> findUserByEmailAddress(String emailAddress) {
        return userRepository.findUserByEmailAddress(emailAddress);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public String createJwtToken(long userId, String[] scopes) {
        return new Jwt()
                .addClaim("userId", userId)
                .addClaim("scopes", scopes)
                .setSecret(jwtSecret.getBytes())
                .setStartDate(System.currentTimeMillis())
                .setExpirationDate(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(5))
                .setAlgorithm("HmacSHA256")
                .buildToken();
    }

    public String createRefreshToken(long userId) {
        return new Jwt()
                .addClaim("userId", userId)
                .setSecret(jwtSecret.getBytes())
                .setStartDate(System.currentTimeMillis())
                .setExpirationDate(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(5))
                .setAlgorithm("HmacSHA256")
                .buildToken();
    }
}
