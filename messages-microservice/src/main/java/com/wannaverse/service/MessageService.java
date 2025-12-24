package com.wannaverse.service;

import com.wannaverse.persistence.Message;
import com.wannaverse.persistence.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveNewMessage(Message message) {
        messageRepository.save(message);
    }

    public Slice<Message> getMessages(String channel, int page, int size) {
        return messageRepository.findByKeyChannelId(channel, PageRequest.of(page, size));
    }
}
