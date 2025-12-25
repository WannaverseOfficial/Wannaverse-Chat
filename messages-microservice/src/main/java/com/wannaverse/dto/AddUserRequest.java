package com.wannaverse.dto;

import lombok.Data;

@Data
public class AddUserRequest {
    private String channelId;
    private String userId;
}
