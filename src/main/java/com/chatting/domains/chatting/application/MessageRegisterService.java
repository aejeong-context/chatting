package com.chatting.domains.chatting.application;

import com.chatting.domains.chatting.domain.MessageEntity;
import com.chatting.domains.chatting.domain.MessageRepository;
import com.chatting.domains.chatting.domain.RoomEntity;
import com.chatting.domains.chatting.domain.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class MessageRegisterService {
  private final MessageRepository messageRepository;
  private final RoomRepository roomRepository;

  @Transactional
  public ResponseEntity registerMessage(String senderName, String roomId, String message) {
    RoomEntity roomEntity = roomRepository.findByRoomId(roomId);
    MessageEntity messageEntity =
        messageRepository.save(
            MessageEntity.builder()
                .message(message)
                .roomId(roomEntity.getRoomId())
                .senderName(senderName)
                .build());
    return ResponseEntity.ok(messageEntity);
  }
}
