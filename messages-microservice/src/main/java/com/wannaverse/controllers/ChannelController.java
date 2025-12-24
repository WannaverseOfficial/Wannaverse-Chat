package com.wannaverse.controllers;

import com.wannaverse.persistence.Channel;
import com.wannaverse.service.ChannelService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelService channelService;

    @Autowired
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createChannel(@Valid @RequestBody Channel channel) {
        channel.setCreationDate(System.currentTimeMillis());

        channelService.save(channel);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
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
