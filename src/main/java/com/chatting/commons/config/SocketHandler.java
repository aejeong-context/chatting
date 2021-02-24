package com.chatting.commons.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;

@Component
public class SocketHandler extends TextWebSocketHandler {
    private Logger log = LogManager.getLogger(SocketHandler.class);

    HashMap<String,WebSocketSession> sessionHashMap = new HashMap<>(); // 세션을 담아둘 map

    // 메시지를 수신하면 실행
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        log.info("message.getPayload is {}",msg);
        for(String key : sessionHashMap.keySet()){
            WebSocketSession wss = sessionHashMap.get(key);
            try{
                wss.sendMessage(new TextMessage(msg));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // 웹소켓 연결이 되면 동작
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessionHashMap.put(session.getId(),session);
        log.info("sessionMap is {}",sessionHashMap.entrySet());
    }
    // 웹소켓 종료되면 동작
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionHashMap.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }
}
