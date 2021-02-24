package com.chatting.domains.chatting.presentation.application.dto;

import lombok.Getter;

@Getter
public class MessageRequest {
    private String messageType;
    private String roomId;
    private String senderName;
    private String message;
}
