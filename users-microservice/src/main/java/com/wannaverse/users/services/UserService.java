package com.wannaverse.users.services;

import com.wannaverse.users.persistence.User;
import com.wannaverse.users.persistence.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findUserById(id);
    }

    public Page<User> search(String query, int page, int size) {
        return userRepository
                .findUserByFirstNameIgnoreCaseOrLastNameIgnoreCaseOrDisplayNameIgnoreCase(
                        query, query, query, PageRequest.of(page, size));
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void updateUser(User savedUser, User update) {
        savedUser.setFirstName(update.getFirstName());
        savedUser.setLastName(update.getLastName());
        savedUser.setDisplayName(update.getDisplayName());
        savedUser.setAbout(update.getAbout());
        save(savedUser);
    }

    public void createUser(String userId, User initial) {
        initial.setId(userId);
        initial.setCreationDate(System.currentTimeMillis());
        save(initial);
    }
}
