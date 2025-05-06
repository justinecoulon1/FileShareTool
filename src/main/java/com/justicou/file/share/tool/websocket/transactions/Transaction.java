package com.justicou.file.share.tool.websocket.transactions;

public record Transaction(Long id, Long downloadingUserId, Long uploadingUserId, String fileName) {
}
