package com.wannaverse.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

@PrimaryKeyClass
@Data
@NoArgsConstructor
public class MessageKey {

    @PrimaryKeyColumn(name = "channel_id", type = PrimaryKeyType.PARTITIONED)
    private String channelId;

    @PrimaryKeyColumn(
            name = "message_id",
            type = PrimaryKeyType.CLUSTERED,
            ordering = Ordering.DESCENDING)
    private UUID messageId;
}
