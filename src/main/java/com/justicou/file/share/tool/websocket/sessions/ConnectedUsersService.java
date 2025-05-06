package com.justicou.file.share.tool.websocket.sessions;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConnectedUsersService {

    Map<Long, WebSocketSession> webSocketSessionByUserId = Collections.synchronizedMap(new HashMap<>());

    public void addConnectedUser(Long userId, WebSocketSession session) {
        webSocketSessionByUserId.put(userId, session);
    }

    public boolean removeConnectedUser(Long userId) {
        try (WebSocketSession session = webSocketSessionByUserId.remove(userId)) {
            return session != null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WebSocketSession getUserSession(Long userId) {
        return webSocketSessionByUserId.get(userId);
    }

    public List<Long> getAllConnectedUserIds() {
        return webSocketSessionByUserId.keySet().stream().toList();
    }
}
