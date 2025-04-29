package com.justicou.file.share.tool.rest.utils.auth;

import com.justicou.file.share.tool.configuration.TokenProperties;
import com.justicou.file.share.tool.exceptions.ForbiddenException;
import com.justicou.file.share.tool.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {

    private static final long ACCESS_TOKEN_EXPIRATION_MS = 15L * 60 * 1000 * 1000;
    private static final long REFRESH_TOKEN_EXPIRATION_MS = 48L * 60 * 60 * 1000;

    private final SecretKey secretKey;

    public TokenService(TokenProperties tokenProperties) {
        this.secretKey = Keys.hmacShaKeyFor(tokenProperties.getSecret().getBytes());
    }

    public String generateAccessToken(Long userId) {
        return generateToken(TokenType.ACCESS, userId, ACCESS_TOKEN_EXPIRATION_MS);
    }

    public String generateRefreshToken(Long userId) {
        return generateToken(TokenType.REFRESH, userId, REFRESH_TOKEN_EXPIRATION_MS);
    }

    private String generateToken(TokenType tokenType, Long userId, long duration) {
        long creationTime = System.currentTimeMillis();
        long expirationTime = creationTime + duration;
        return Jwts.builder()
                .claim("userId", userId)
                .claim("tokenType", tokenType)
                .issuedAt(new Date(creationTime))
                .expiration(new Date(expirationTime))
                .signWith(this.secretKey)
                .compact();
    }

    public TokenInformation getTokenInformation(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String tokenTypeStr = claims.get("tokenType", String.class);
            return new TokenInformation(claims.get("userId", Long.class), TokenType.valueOf(tokenTypeStr), token);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("Token has expired");
        } catch (Exception e) {
            throw new ForbiddenException();
        }

    }
}
