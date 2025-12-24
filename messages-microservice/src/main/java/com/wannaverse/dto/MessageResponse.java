package com.wannaverse.dto;

import com.wannaverse.persistence.Message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MessageResponse {
    private List<Message> messages;
    private int pageNumber;
    private boolean hasNext;
}
