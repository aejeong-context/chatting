package com.chatting.commons.config;

import com.chatting.domains.chatting.presentation.application.dto.MessageRequest;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SocketHandler extends TextWebSocketHandler {
  private final ObjectMapper objectMapper;
  private Logger log = LogManager.getLogger(SocketHandler.class);

  // HashMap<String, WebSocketSession> sessionHashMap = new HashMap<>(); // 세션을 담아둘 map
  List<HashMap<String, Object>> sessionList = new ArrayList<>();
  // 메시지를 수신하면 실행
  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String msg = message.getPayload();
    JSONObject jsonObject = jsonToObjectParser(msg);
    log.info("message.getPayload is {}", msg);

    String room = (String) jsonObject.get("roomId");
    HashMap<String, Object> temp = new HashMap<String, Object>();
    if (sessionList.size() > 0) {
      for (HashMap<String, Object> sessionOne : sessionList) {
        String roomId = (String) sessionOne.get("roomId");
        if (roomId.equals((room))) {
          temp = sessionOne;
          break;
        }
      }
      for (String k : temp.keySet()) {
        if (k.equals("roomId")) {
          continue;
        }
        WebSocketSession wss = (WebSocketSession) temp.get(k);
        if (wss != null) {
          try {
            wss.sendMessage(new TextMessage(jsonObject.toJSONString()));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }

    //    for (String key : sessionHashMap.keySet()) {
    //      WebSocketSession wss = sessionHashMap.get(key);
    //      log.info("sessionHashMap.getKey : {}", wss);
    //      try {
    //
    //        log.info(
    //            "objectMapper.writeValueAsString(message) : {}",
    //            objectMapper.writeValueAsString(message),
    //            MessageRequest.class);
    //        log.info("jsonObject.toJSONString() : {}", jsonObject);
    //        wss.sendMessage(new TextMessage(jsonObject.toJSONString()));
    //      } catch (Exception e) {
    //        e.printStackTrace();
    //      }
    //    }
    //  }
  }

  // 웹소켓 연결이 되면 동작
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    super.afterConnectionEstablished(session);
    String url = session.getUri().toString();
    log.info("ulr is {}", url);
    String roomId = url.split("/chatting/")[1];
    int inx = sessionList.size();
    boolean flog = false;
    if (sessionList.size() > 0) {
      inx += 1;
      for (HashMap<String, Object> sessionOne : sessionList) {
        String room = (String) sessionOne.get("roomId");
        if (room.equals(roomId)) {
          flog = true;
          HashMap<String, Object> map = sessionOne;
          map.put(session.getId(), session);
          break;
        }
        if (flog) {
          HashMap<String, Object> map = sessionList.get(inx);
          map.put(session.getId(), session);

        } else {
          HashMap<String, Object> map = new HashMap<String, Object>();
          map.put("roomId", roomId);
          map.put(session.getId(), session);
          sessionList.add(map);
        }
        JSONObject object = new JSONObject();
        object.put("type", "getId");
        object.put("sessionId", session.getId());
        session.sendMessage(new TextMessage(object.toJSONString()));
      }
    }
  }
  // 웹소켓 종료되면 동작
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
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
