package com.justicou.file.share.tool.configuration;

import com.justicou.file.share.tool.websocket.FileShareToolHandshakeHandler;
import com.justicou.file.share.tool.websocket.FileShareWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final FileShareToolHandshakeHandler handshakeHandler;
    private final FileShareWebSocketHandler webSocketHandler;

    public WebSocketConfig(FileShareToolHandshakeHandler handshakeHandler, FileShareWebSocketHandler webSocketHandler) {
        this.handshakeHandler = handshakeHandler;
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                .addHandler(webSocketHandler, "/hello")
                .setAllowedOrigins("*")
                .setHandshakeHandler(handshakeHandler);
    }
}

