package com.wannaverse.users.services;

import com.wannaverse.users.persistence.User;
import com.wannaverse.users.persistence.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isEmailAddressInUse(String email) {
        return userRepository.findUserByEmailAddress(email).isPresent();
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
