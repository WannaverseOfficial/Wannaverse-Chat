package com.wannaverse.controllers;

import com.wannaverse.persistence.Channel;
import com.wannaverse.service.ChannelService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelService channelService;

    @Autowired
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PutMapping("/")
    public ResponseEntity<?> createChannel(
            @Valid @RequestBody Channel channel, Principal principal) {
        channel.setCreationDate(System.currentTimeMillis());

        if (channel.getOwnerId().isEmpty()) {
            channel.setOwnerId(principal.getName());
        }

        channelService.save(channel);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> updateChannel(@Valid @RequestBody Channel channel) {
        channelService.save(channel);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteChannel(String channelId) {
        channelService.getChannelById(channelId).ifPresent(channelService::delete);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<?> search(
            String query,
            Optional<Integer> page,
            Optional<Integer> size,
            Optional<String> channelId) {
        if (channelId.isEmpty()) {
            return ResponseEntity.ok(channelService.search(query, page.orElse(0), size.orElse(10)));
        }

        Optional<Channel> optionalChannel = channelService.getChannelById(channelId.get());

        if (optionalChannel.isPresent()) {
            return ResponseEntity.ok(optionalChannel.get());
        }

        return ResponseEntity.ok().build();
    }
}
