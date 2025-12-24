package com.wannaverse.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@NoArgsConstructor
@Data
@Table("messages_by_channel")
public class Message {

    @PrimaryKey private MessageKey key;

    private String userId;
    private String content;
    private String mimetype;
}
