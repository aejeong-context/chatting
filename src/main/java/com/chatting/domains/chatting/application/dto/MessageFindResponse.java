package com.chatting.domains.chatting.application.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MessageFindResponse {
  private String senderName;
  private String message;
  private int read;
  private String roomId;
}
