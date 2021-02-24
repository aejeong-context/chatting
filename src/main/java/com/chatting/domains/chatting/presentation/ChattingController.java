package com.chatting.domains.chatting.presentation;

import com.chatting.domains.auction.domain.AuctionEntity;
import com.chatting.domains.chatting.domain.RoomEntity;
import com.chatting.domains.chatting.domain.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class ChattingController {

  private final RoomRepository roomRepository;

  @RequestMapping("/chat")
  public String chat(Model model) {
    model.addAttribute("name", "aejeong");
    return "chat";
  }

  @RequestMapping("/room")
  public String room() {
    return "room";
  }

  @RequestMapping("/createRoom")
  public @ResponseBody RoomEntity createRoom(@RequestParam HashMap<Object, Object> params) {
    System.out.println("init");
    String uuid = UUID.randomUUID().toString();
    String roomName = (String) params.get("roomName");
    //    AuctionEntity auctionEntity = AuctionEntity.builder().productName(roomName).build();
    RoomEntity roomEntity = null;
    if (roomName != null && !roomName.trim().equals("")) {
      roomEntity =
          roomRepository.save(
              RoomEntity.builder()
                  .roomId(uuid)
                  .name(roomName)
                  //                  .auctionEntity(auctionEntity)
                  .build());
    }
    System.out.println(roomEntity.toString());
    return roomEntity;
  }

  @RequestMapping("/getRoom")
  public @ResponseBody List<RoomEntity> getRoom() {
    System.out.println("getRoom init");

    List<RoomEntity> roomEntityList = roomRepository.findAll();
for(RoomEntity roomEntity : roomEntityList){
  System.out.println("호호잇"+roomEntity);
}
  return roomEntityList;
  }

  @RequestMapping("/moveChatting")
  public String chatting(@RequestParam HashMap<Object, Object> params, Model model) {
    System.out.println("moveChatting init");

    String roomId = (String) params.get("roomId");
    RoomEntity roomEntity = roomRepository.findByRoomId(roomId);
    System.out.println("move"+roomEntity.getRoomId());
    if (roomEntity != null) {
      model.addAttribute("roomName", params.get("roomName"));
      model.addAttribute("roomId", params.get("roomId"));
      return "chat";
    } else {
      return "room";
    }
  }
}
