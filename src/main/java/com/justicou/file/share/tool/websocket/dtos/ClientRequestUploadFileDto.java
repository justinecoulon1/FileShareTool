package com.justicou.file.share.tool.websocket.dtos;

public record ClientRequestUploadFileDto(String sharedFileName, Long transactionId, FileContentDto fileContent) {
}

