package com.wannaverse.controllers;

import com.wannaverse.dto.AddUserRequest;
import com.wannaverse.persistence.Channel;
import com.wannaverse.persistence.Visibility;
import com.wannaverse.service.ChannelService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/channel")
public class ChannelController {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_NUMBER_OF_SEARCH_RESULTS = 10;
    private final ChannelService channelService;

    @Autowired
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PutMapping
    public ResponseEntity<?> createChannel(
            @Valid @RequestBody Channel channel, Principal principal) {
        channel.setCreationDate(System.currentTimeMillis());

        if (channel.getOwnerId() == null
                || channel.getOwnerId().isEmpty()
                || !channel.getOwnerId().equals(principal.getName())) {
            channel.setOwnerId(principal.getName());
        }

        channelService.save(channel);

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> updateChannel(@Valid @RequestBody Channel channel) {
        channelService.save(channel);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-user")
    public ResponseEntity<?> addUserToChannel(@RequestBody AddUserRequest request) {
        Optional<Channel> optionalChannel = channelService.getChannelById(request.getChannelId());

        if (optionalChannel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Channel channel = optionalChannel.get();
        channel.getUsers().add(request.getUserId());
        channelService.save(channel);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteChannel(String channelId) {
        channelService.getChannelById(channelId).ifPresent(channelService::delete);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> search(
            String query,
            Optional<Integer> page,
            Optional<Integer> size,
            Optional<String> channelId,
            Principal principal) {
        if (channelId.isEmpty()) {
            return ResponseEntity.ok(
                    channelService.search(
                            query,
                            page.orElse(DEFAULT_PAGE),
                            size.orElse(DEFAULT_NUMBER_OF_SEARCH_RESULTS)));
        }

        Optional<Channel> optionalChannel = channelService.getChannelById(channelId.get());

        if (optionalChannel.isEmpty()) {
            return ResponseEntity.ok().build();
        }

        Channel channel = optionalChannel.get();
        List<String> users = channel.getUsers();

        if (!users.contains(principal.getName()) && channel.getVisibility() == Visibility.PRIVATE) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok(optionalChannel.get());
    }
}
