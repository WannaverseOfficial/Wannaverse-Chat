package com.wannaverse.users.services;

import com.wannaverse.users.persistence.elastic.ElasticUser;
import com.wannaverse.users.persistence.elastic.ElasticUserRepository;
import com.wannaverse.users.persistence.jpa.User;
import com.wannaverse.users.persistence.jpa.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ElasticUserRepository elasticUserRepository;

    @Autowired
    public UserService(UserRepository userRepository, ElasticUserRepository elasticUserRepository) {
        this.userRepository = userRepository;
        this.elasticUserRepository = elasticUserRepository;
    }

    public boolean isEmailAddressInUse(String email) {
        return userRepository.findUserByEmailAddress(email).isPresent();
    }

    public void save(User user) {
        userRepository.save(user);
        elasticUserRepository.save(user.toElasticUser());
    }

    public Optional<User> getUserById(long id) {
        return userRepository.findUserById(id);
    }

    public Page<ElasticUser> search(String query, int page, int size) {
        return elasticUserRepository
                .findUserByFirstNameIgnoreCaseOrLastNameIgnoreCaseOrDisplayNameIgnoreCase(
                        query, query, query, PageRequest.of(page, size));
    }
}
