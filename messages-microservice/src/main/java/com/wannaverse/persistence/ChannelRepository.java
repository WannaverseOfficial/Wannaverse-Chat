package com.wannaverse.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, String> {
    Optional<Channel> findChannelById(String id);

    Page<Channel> findChannelByNameContainingIgnoreCase(String name, Pageable pageable);
}
