package com.wannaverse.persistence;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CassandraRepository<Message, MessageKey> {
    Slice<Message> findByKeyChannelId(String channelId, Pageable pageable);
}
