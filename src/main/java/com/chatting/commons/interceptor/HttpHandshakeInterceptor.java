package com.chatting.commons.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

  private Logger log = LogManager.getLogger(HttpHandshakeInterceptor.class);

  @Override
  public boolean beforeHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      Map<String, Object> attributes)
      throws Exception {
    if (request instanceof ServletServerHttpRequest) {
      ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
      HttpServletRequest req = servletRequest.getServletRequest();
      String userName = req.getParameter("userName");
      log.info("Interceptor user Name is {}", userName);

      attributes.put("userName", userName);
      //      String userName = (String) req.getSession().getAttribute("userName");
      //      log.info("Interceptor user Name is {}", userName);
      //      attributes.put("userName", userName);
    }
    return true;
  }

  @Override
  public void afterHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      Exception exception) {}
}
