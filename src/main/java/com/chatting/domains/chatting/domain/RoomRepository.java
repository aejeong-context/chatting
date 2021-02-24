package com.chatting.domains.chatting.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity,Long> {

    RoomEntity findByRoomId(String roomId);
}
