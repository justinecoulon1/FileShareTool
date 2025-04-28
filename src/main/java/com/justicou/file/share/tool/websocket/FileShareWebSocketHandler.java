package com.justicou.file.share.tool.websocket;

import com.justicou.file.share.tool.exceptions.ForbiddenException;
import com.justicou.file.share.tool.rest.users.connectedusers.ConnectedUsersService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class FileShareWebSocketHandler extends TextWebSocketHandler {

    private final ConnectedUsersService connectedUsersService;

    public FileShareWebSocketHandler(ConnectedUsersService connectedUsersService) {
        this.connectedUsersService = connectedUsersService;
    }

    @Override
    public void
    afterConnectionEstablished(WebSocketSession session) throws Exception {
        FileShareToolPrincipal principal = getPrincipal(session);
        System.out.println(session.getId() + " Connected");
        connectedUsersService.addConnectedUser(principal.getUser().getId(), session);
        session.sendMessage(new TextMessage("Welcome, " + principal.getName()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        FileShareToolPrincipal principal = getPrincipal(session);
        boolean removed = connectedUsersService.removeConnectedUser(principal.getUser().getId());
        System.out.println(session.getId() + " Disconnected success: " + removed);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        FileShareToolPrincipal principal = getPrincipal(session);
        super.handleMessage(session, message);
        Long userId = Long.parseLong((String) message.getPayload());
        WebSocketSession session1 = connectedUsersService.getUserSession(userId);
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

