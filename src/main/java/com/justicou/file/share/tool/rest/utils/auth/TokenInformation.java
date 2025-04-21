package com.justicou.file.share.tool.rest.utils.auth;

public class TokenInformation {

    private final Long userId;
    private final TokenType tokenType;
    private final String token;

    public TokenInformation(Long userId, TokenType tokenType, String token) {
        this.userId = userId;
        this.tokenType = tokenType;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getToken() {
        return token;
    }
}
