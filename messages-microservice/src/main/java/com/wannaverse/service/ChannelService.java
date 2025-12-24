package com.wannaverse.service;

import com.wannaverse.persistence.Channel;
import com.wannaverse.persistence.ChannelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelService {
    private final ChannelRepository channelRepository;

    @Autowired
    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public void save(Channel channel) {
        channelRepository.save(channel);
    }
}
