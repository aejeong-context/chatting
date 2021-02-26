package com.chatting.domains.chatting.application;

import com.chatting.domains.chatting.application.dto.MessageFindResponse;
import com.chatting.domains.chatting.domain.MessageEntity;
import com.chatting.domains.chatting.domain.MessageRepository;
import com.chatting.domains.chatting.domain.RoomEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageFindService {
  private final MessageRepository messageRepository;

  public List<MessageFindResponse> findAllMessage(String roomId) {

    List<MessageEntity> messageEntityList = messageRepository.findAllByRoomId(roomId);
    List<MessageFindResponse> messageFindResponseList = new LinkedList<>();

    for (MessageEntity messageEntity : messageEntityList) {
      messageFindResponseList.add(
          MessageFindResponse.builder()
              .message(messageEntity.getMessage())
              .senderName(messageEntity.getSenderName())
              .read(messageEntity.getRead())
              .roomId(messageEntity.getRoomId())
              .build());
    }
    return messageFindResponseList;
  }
}
