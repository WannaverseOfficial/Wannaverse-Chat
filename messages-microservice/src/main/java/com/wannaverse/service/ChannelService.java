package com.wannaverse.service;

import com.wannaverse.persistence.Channel;
import com.wannaverse.persistence.ChannelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Page<Channel> search(String query, int page, int size) {
        return channelRepository.findChannelByNameContainingIgnoreCase(
                query, PageRequest.of(page, size));
    }

    public Optional<Channel> getChannelById(String channelId) {
        return channelRepository.findChannelById(channelId);
    }
}
