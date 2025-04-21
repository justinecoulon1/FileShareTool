package com.justicou.file.share.tool.websocket;

import com.justicou.file.share.tool.db.model.FileShareToolUser;
import com.justicou.file.share.tool.db.repositories.UsersRepository;
import com.justicou.file.share.tool.exceptions.ForbiddenException;
import com.justicou.file.share.tool.rest.utils.auth.TokenInformation;
import com.justicou.file.share.tool.rest.utils.auth.TokenService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
public class FileShareToolHandshakeHandler extends DefaultHandshakeHandler {
    private final TokenService tokenService;
    private final UsersRepository usersRepository;

    public FileShareToolHandshakeHandler(TokenService tokenService, UsersRepository usersRepository) {
        this.tokenService = tokenService;
        this.usersRepository = usersRepository;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String accessToken = request.getHeaders().getFirst("accessToken");

        if (accessToken == null) {
            throw new ForbiddenException("Missing token");
        }

        TokenInformation accessTokenInformation = tokenService.getTokenInformation(accessToken);
        FileShareToolUser user = usersRepository.findById(accessTokenInformation.getUserId())
                .orElseThrow(ForbiddenException::new);

        return new FileShareToolPrincipal(user);
    }

}
