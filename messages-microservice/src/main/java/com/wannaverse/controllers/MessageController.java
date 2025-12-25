package com.wannaverse.controllers;

import com.wannaverse.dto.CreateMessageRequest;
import com.wannaverse.dto.MessageResponse;
import com.wannaverse.persistence.Channel;
import com.wannaverse.persistence.Message;
import com.wannaverse.persistence.MessageKey;
import com.wannaverse.persistence.Visibility;
import com.wannaverse.service.ChannelService;
import com.wannaverse.service.MessageService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/message")
public class MessageController {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_NUMBER_OF_MESSAGES_TO_RETURN = 100;
    private final MessageService messageService;
    private final ChannelService channelService;

    @Autowired
    public MessageController(
            MessageService messageService,
            ChannelService channelService,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.messageService = messageService;
        this.channelService = channelService;
    }

    @PutMapping
    @Transactional
    public ResponseEntity<?> addMessageToChannel(
            @RequestBody CreateMessageRequest request, Principal principal) {
        Optional<Channel> optionalChannel = channelService.getChannelById(request.getChannelId());

        if (optionalChannel.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String userId = principal.getName();
        Channel channel = optionalChannel.get();

        if (!channel.getUsers().contains(userId) && channel.getVisibility() == Visibility.PRIVATE) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!channel.getUsers().contains(userId)) {
            channel.getUsers().add(userId);
            channelService.save(channel);
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

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getMessagesInChannel(
            String channelId, Optional<Integer> page, Optional<Integer> size, Principal principal) {
        Optional<Channel> optionalChannel = channelService.getChannelById(channelId);

        if (optionalChannel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Channel channel = optionalChannel.get();

        if (!channel.getUsers().contains(principal.getName())
                && channel.getVisibility() == Visibility.PRIVATE) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Slice<Message> slice =
                messageService.getMessages(
                        channelId,
                        page.orElse(DEFAULT_PAGE),
                        size.orElse(DEFAULT_NUMBER_OF_MESSAGES_TO_RETURN));

        return ResponseEntity.ok(
                new MessageResponse(
                        slice.getContent(), slice.getPageable().getPageNumber(), slice.hasNext()));
    }
}
