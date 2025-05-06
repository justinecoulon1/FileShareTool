package com.justicou.file.share.tool.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justicou.file.share.tool.exceptions.ForbiddenException;
import com.justicou.file.share.tool.rest.sharedfiles.SharedFileInfoService;
import com.justicou.file.share.tool.websocket.dtos.*;
import com.justicou.file.share.tool.websocket.sessions.ConnectedUsersService;
import com.justicou.file.share.tool.websocket.transactions.FileTransactionsService;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Objects;

@Component
public class FileShareToolSocketHandler extends TextWebSocketHandler {

    private final ConnectedUsersService connectedUsersService;
    private final SharedFileInfoService sharedFileInfoService;
    private final FileTransactionsService fileTransactionsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public FileShareToolSocketHandler(
            ConnectedUsersService connectedUsersService,
            SharedFileInfoService sharedFileInfoService,
            FileTransactionsService fileTransactionsService
    ) {
        this.connectedUsersService = connectedUsersService;
        this.sharedFileInfoService = sharedFileInfoService;
        this.fileTransactionsService = fileTransactionsService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        FileShareToolPrincipal principal = getPrincipal(session);
        System.out.println(session.getId() + " Connected");
        connectedUsersService.addConnectedUser(principal.getUser().getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        FileShareToolPrincipal principal = getPrincipal(session);
        boolean removed = connectedUsersService.removeConnectedUser(principal.getUser().getId());
        System.out.println(session.getId() + " Disconnected success: " + removed);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        SocketMessage socketMessage = objectMapper.readValue(message.getPayload(), SocketMessage.class);
        handleClientSocketMessage(session, socketMessage);
    }

    private String handleClientSocketMessage(WebSocketSession currentUserSession, SocketMessage socketMessage) throws IOException {
        return switch (socketMessage.type()) {
            case REQUEST_FILE ->
                    handleClientRequestFileMessage(currentUserSession, objectMapper.convertValue(socketMessage.messageContent(), ClientRequestDownloadFileDto.class));
            case SEND_FILE ->
                    handleClientRequestSendFileMessage(currentUserSession, objectMapper.convertValue(socketMessage.messageContent(), ClientRequestUploadFileDto.class));
        };
    }

    private String handleClientRequestFileMessage(
            WebSocketSession currentUserSession,
            ClientRequestDownloadFileDto clientRequestDownloadFileDto
    ) throws IOException {
        var uploadingUserSession = connectedUsersService.getUserSession(clientRequestDownloadFileDto.userId());
        var sharedFileInfoName = sharedFileInfoService.getSharedFileInfoById(clientRequestDownloadFileDto.sharedFileId()).getName();
        var principal = getPrincipal(currentUserSession);
        var transaction = fileTransactionsService.createTransaction(principal.getUser().getId(), clientRequestDownloadFileDto.userId(), sharedFileInfoName);
        sendSocketMessage(uploadingUserSession, new SocketMessage(SocketMessageTypes.SEND_FILE, new ServerRequestUploadFileDto(sharedFileInfoName, transaction.id())));
        return "handle file request";
    }

    private String handleClientRequestSendFileMessage(
            WebSocketSession currentUserSession,
            ClientRequestUploadFileDto clientRequestUploadFileDto
    ) throws IOException {
        var transaction = fileTransactionsService.getTransactionById(clientRequestUploadFileDto.transactionId());
        var principal = getPrincipal(currentUserSession);
        if (transaction == null || !Objects.equals(transaction.uploadingUserId(), principal.getUser().getId())) {
            throw new BadRequestException();
        }

        var sharedFileInfoName = transaction.fileName();
        var downloadingUserSession = connectedUsersService.getUserSession(transaction.downloadingUserId());
        sendSocketMessage(downloadingUserSession, new SocketMessage(SocketMessageTypes.REQUEST_FILE,
                new ServerRequestDownloadFileDto(sharedFileInfoName, clientRequestUploadFileDto.fileContent())));
        return "handle file request";
    }

    private void sendSocketMessage(WebSocketSession session, SocketMessage message) throws IOException {
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
    }

    private FileShareToolPrincipal getPrincipal(WebSocketSession session) {
        FileShareToolPrincipal principal = (FileShareToolPrincipal) session.getPrincipal();
        if (principal == null) {
            throw new ForbiddenException();
        }
        return principal;
    }
}

