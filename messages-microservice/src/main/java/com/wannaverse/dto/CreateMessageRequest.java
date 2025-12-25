package com.wannaverse.dto;

import lombok.Data;

@Data
public class CreateMessageRequest {
    private String channelId;
    private String content;
    private String mimetype;
}
