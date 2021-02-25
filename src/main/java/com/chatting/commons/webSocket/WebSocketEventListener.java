package com.chatting.commons.webSocket;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RequiredArgsConstructor
@Component
public class WebSocketEventListener {
  private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

//  private final SimpMessageSendingOperations messagingTemplate;

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    logger.info("Received a new web socket connection");
  }

  @EventListener
  public void handleWebSocketDisConnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

    String username = (String) headerAccessor.getSessionAttributes().get("username");
    if (username != null) {
      logger.info("User Disconnected : " + username);

    }
  }
  //
  //    /**
  //     * Find session id by session id.
  //     *
  //     * @param sessionId
  //     * @return
  //     */
  //    public String findBrowserSessionId(String sessionId) {
  //        String session = null;
  //
  //        for (Map.Entry<String, BrowserSession> entry : browserSessionMap.entrySet()) {
  //            if (entry.getKey().equals(sessionId)) {
  //                session = entry.getKey();
  //            }
  //        }
  //
  //        return session;
  //    }
  //
  //    /**
  //     * Register browser session.
  //     *
  //     * @param browserSession the browser session
  //     * @param sessionId      the session id
  //     */
  //    public synchronized void registerBrowserSession(BrowserSession browserSession, String
  // sessionId) {
  //        browserSessionMap.put(sessionId, browserSession);
  //    }
  //
  //    /**
  //     * Find session ids by user name list.
  //     *
  //     * @param username the member id
  //     * @return the list
  //     */
  //    public List<String> findSessionIdsByMemberId(String username) {
  //        List<String> sessionIdList = new ArrayList<String>();
  //
  //        for (Map.Entry<String, BrowserSession> entry : browserSessionMap.entrySet()) {
  //            if (entry.getValue().getUserId().equals(username)) {
  //                sessionIdList.add(entry.getKey());
  //            }
  //        }
  //
  //        return sessionIdList;
  //    }
  //
  //    /**
  //     * Create headers message headers.
  //     *
  //     * @param sessionId the session id
  //     * @return the message headers
  //     */
  //    public MessageHeaders createHeaders(String sessionId) {
  //        SimpMessageHeaderAccessor headerAccessor =
  // SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
  //        headerAccessor.setSessionId(sessionId);
  //        headerAccessor.setLeaveMutable(true);
  //
  //        return headerAccessor.getMessageHeaders();
  //    }

}
