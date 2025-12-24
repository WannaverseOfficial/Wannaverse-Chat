package com.wannaverse.controllers;

import com.wannaverse.dto.CreateMessageRequest;
import com.wannaverse.dto.MessageResponse;
import com.wannaverse.persistence.Channel;
import com.wannaverse.persistence.Message;
import com.wannaverse.persistence.MessageKey;
import com.wannaverse.service.ChannelService;
import com.wannaverse.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import tools.jackson.databind.ObjectMapper;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/message")
public class MessageController {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final MessageService messageService;
    private final ChannelService channelService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public MessageController(
            MessageService messageService,
            ChannelService channelService,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.messageService = messageService;
        this.channelService = channelService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PutMapping
    public ResponseEntity<?> addMessageToChannel(
            @RequestBody CreateMessageRequest request, Principal principal) {
        Optional<Channel> optionalChannel = channelService.getChannelById(request.getChannelId());

        if (optionalChannel.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String userId = principal.getName();
        Channel channel = optionalChannel.get();

        if (!channel.getUsers().contains(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Message message = new Message();
        MessageKey key = new MessageKey();
        key.setChannelId(request.getChannelId());
        key.setMessageId(UUID.randomUUID());

        message.setKey(key);
        message.setUserId(userId);
        message.setContent(request.getContent());
        message.setMimetype(request.getMimetype());

        messageService.saveNewMessage(message);

        kafkaTemplate.send("new-message-in-channel", OBJECT_MAPPER.writeValueAsString(message));

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getMessagesInChannel(
            String channelId, Optional<Integer> page, Optional<Integer> size) {
        Slice<Message> slice =
                messageService.getMessages(channelId, page.orElse(0), size.orElse(100));
        List<Message> messages = slice.getContent();
        int pageNumber = slice.getPageable().getPageNumber();
        boolean hasNext = slice.hasNext();

        return ResponseEntity.ok(new MessageResponse(messages, pageNumber, hasNext));
    }
}
