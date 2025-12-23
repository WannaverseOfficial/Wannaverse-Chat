package com.wannaverse.users;

import com.wannaverse.users.persistence.User;
import com.wannaverse.users.services.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private final UserService userService;

    @Autowired
    public KafkaConsumer(UserService userService) {
        this.userService = userService;
    }

    @KafkaListener(topics = "new-user-registration")
    public void consume(String userId) {
        logger.info("Kafka received message on new-user-registration: {}", userId);

        userService.createUser(userId, new User());
    }
}
