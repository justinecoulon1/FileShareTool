package com.justicou.file.share.tool.websocket;

import com.justicou.file.share.tool.exceptions.ForbiddenException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class FileShareWebSocketHandler extends TextWebSocketHandler {

    Map<Long, WebSocketSession> webSocketSessionByUserId = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void
    afterConnectionEstablished(WebSocketSession session) throws Exception {
        FileShareToolPrincipal principal = getPrincipal(session);
        System.out.println(session.getId() + " Connected");
        webSocketSessionByUserId.put(principal.getUser().getId(), session);
        session.sendMessage(new TextMessage("Welcome, " + principal.getName()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        FileShareToolPrincipal principal = getPrincipal(session);
        System.out.println(session.getId() + " Disconnected");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        FileShareToolPrincipal principal = getPrincipal(session);
        super.handleMessage(session, message);
        Long userId = Long.parseLong((String) message.getPayload());
        WebSocketSession session1 = webSocketSessionByUserId.get(userId);
        FileShareToolPrincipal principal1 = getPrincipal(session1);
        session1.sendMessage(new TextMessage(principal.getName() + " want your file"));
        session.sendMessage(new TextMessage(principal1.getName() + " will send file"));
    }

    private FileShareToolPrincipal getPrincipal(WebSocketSession session) {
        FileShareToolPrincipal principal = (FileShareToolPrincipal) session.getPrincipal();
        if (principal == null) {
            throw new ForbiddenException();
        }
        return principal;
    }
}

