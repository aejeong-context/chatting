package com.chatting.commons.webSocket;

import com.chatting.domains.chatting.application.MessageRegisterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
public class SocketHandler extends TextWebSocketHandler { private Logger log = LogManager.getLogger(SocketHandler.class);

  // 세션을 담아둔다
  List<HashMap<String, Object>> sessionList = new ArrayList<>();
  // 유저 정보 세션을 담아둔다.
  Map<String, WebSocketSession> users = new ConcurrentHashMap<String, WebSocketSession>();

  private final MessageRegisterService messageRegisterService;

  // 메시지를 수신하면 실행
  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {


    log.info("Start handle Text Message ===================");
    Map<String, Object> httpSession = session.getAttributes();
    log.info(httpSession);
    String user = httpSession.get("userName").toString();
    users.put(user, session);
    if (user.equals("애정")) {
      TextMessage textMessage = new TextMessage("나니?");
      WebSocketSession webSocketSession = users.get("지수");
      log.info("지수 일 떄 {}", users.get("지수"));
      webSocketSession.sendMessage(textMessage);
    }

    String msg = message.getPayload();
    JSONObject jsonObject = jsonToObjectParser(msg);
    log.info("message.getPayload is {}", msg);

    String userName = (String) jsonObject.get("userName");
    String room = (String) jsonObject.get("roomId");
    String msgContents = (String) jsonObject.get("msg");

    log.info("request roomId {}", room);
    log.info("handleTextMessage -현재 세션리스트의 갯수는 ? {}", sessionList.size());
    if (sessionList.size() > 0) {

      for (HashMap<String, Object> sessionOne : sessionList) {

        String roomId = (String) sessionOne.get("roomId");
        log.info("== sessionOne == / roomId is : {} ", roomId);
        if (room.equals(roomId)) {
          WebSocketSession wss = (WebSocketSession) sessionOne;
          if (wss != null) {
            try {
              wss.sendMessage(new TextMessage(jsonObject.toJSONString()));
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }

    messageRegisterService.registerMessage(userName, room, msgContents);
  }

  // 웹소켓 연결이 되면 동작
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("after Connection Established ===========");

    log.info("afterConnectionEstablished -현재 세션리스트의 갯수는 ? {}", sessionList.size());
    log.info(" session이 뭘 갖고 있는지 궁금하군 {}", session.getAttributes());

    String url = session.getUri().toString();
    log.info("ulr is {}", url);
    String roomId = url.split("/chatting/")[1];

    int idx = sessionList.size();
    boolean flog = false;
    if (sessionList.size() > 0) {
      log.info("세션이 있다고? -현재 세션리스트의 갯수는 ? {}", sessionList.size());

      System.out.println("세션이 있어!");
      for (int i = 0; i < sessionList.size(); i++) {
        String room = (String) sessionList.get(i).get("roomId");
        if (room.equals(roomId)) {
          System.out.println("같은 룸아이디인데!?");
          flog = true;
          idx = i;
          break;
        }
      }
    }

    if (flog) {
      HashMap<String, Object> map = sessionList.get(idx);
      log.info("같은 룸이 라고?? -현재 세션리스트의 갯수는 ? {}", sessionList.size());

      System.out.println("세션 리스트에 추가해줘야겠다 ");
      map.put(session.getId(), session);

    } else {
      log.info("같은 룸이 아니라고?  -현재 세션리스트의 갯수는 ? {}", sessionList.size());

      HashMap<String, Object> map = new HashMap<String, Object>();
      System.out.println("세션 리스트에 추가해줘야겠다 ");
      map.put("roomId", roomId);
      map.put(session.getId(), session);
      sessionList.add(map);
    }
    log.info("세션이 들어오기만한건가?  -현재 세션리스트의 갯수는 ? {}", sessionList.size());

    JSONObject object = new JSONObject();
    object.put("type", "getId");
    object.put("sessionId", session.getId());
    log.info("sessionId 는 몰까용 ? {} ", session.getId());

    session.sendMessage(new TextMessage(object.toJSONString()));
  }

  // 웹소켓 종료되면 동작
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    log.info("종료될때 세션 갯수는? -현재 세션리스트의 갯수는 ? {}", sessionList.size());

    if (sessionList.size() > 0) {
      for (HashMap<String, Object> sessionOne : sessionList) {
        sessionOne.remove(session.getId());
      }
    }
    super.afterConnectionClosed(session, status);
  }

  private static JSONObject jsonToObjectParser(String jsonStr) {
    JSONParser parser = new JSONParser();
    JSONObject obj = null;
    try {
      obj = (JSONObject) parser.parse(jsonStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return obj;
  }
}
