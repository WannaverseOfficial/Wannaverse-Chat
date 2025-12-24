package com.wannaverse.controllers;

import com.wannaverse.persistence.Channel;
import com.wannaverse.service.ChannelService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelService channelService;

    @Autowired
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createChannel(@Valid @RequestBody Channel channel) {
        channel.setCreationDate(System.currentTimeMillis());

        channelService.save(channel);

        return ResponseEntity.ok().build();
    }
}
