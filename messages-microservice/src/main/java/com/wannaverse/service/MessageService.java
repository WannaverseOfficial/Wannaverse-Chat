package com.wannaverse.service;

import com.wannaverse.persistence.Message;
import com.wannaverse.persistence.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import tools.jackson.databind.ObjectMapper;

@Service
public class MessageService {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String KAFKA_NEW_MESSAGE_TOPIC = "new-message-in-channel";

    private final MessageRepository messageRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public MessageService(
            MessageRepository messageRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.messageRepository = messageRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void saveNewMessage(Message message) {
        messageRepository.save(message);
        kafkaTemplate.send(KAFKA_NEW_MESSAGE_TOPIC, OBJECT_MAPPER.writeValueAsString(message));
    }

    public Slice<Message> getMessages(String channel, int page, int size) {
        return messageRepository.findByKeyChannelId(channel, PageRequest.of(page, size));
    }
}
