package com.finalmelontoken.fmtauthserver.util;

import com.finalmelontoken.fmtauthserver.domain.SecretKey;
import com.finalmelontoken.fmtauthserver.domain.req.RefreshRequest;
import com.finalmelontoken.fmtauthserver.domain.res.TokenResponse;
import com.finalmelontoken.fmtauthserver.exception.GlobalException;
import com.finalmelontoken.fmtauthserver.repository.SecretKeyRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key refreshKey;
    private final SecretKeyRepository secretKeyRepository;

    public JwtTokenProvider(@Value("${jwt.refresh.secret}") String refreshSecretKey, SecretKeyRepository secretKeyRepository) {
        this.secretKeyRepository = secretKeyRepository;
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecretKey);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    public TokenResponse generateRefreshToken(String email) {

        long now = (new Date()).getTime();
        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(now + (60000 * 60 * 24 * 7))) // 1 week
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();

        return TokenResponse.builder()
                .token("Bearer " + refreshToken)
                .build();
    }

    public TokenResponse generateAccessToken(String email, RefreshRequest request) {
        SecretKey secretKey = secretKeyRepository.findByClientKey(request.getClientKey());
        if (secretKey == null)
            throw new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 Client Key");
        byte[] key = Decoders.BASE64.decode(secretKey.getSecretKey());

        long now = (new Date()).getTime();
        String accessToken = Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(now + (60000 * 60))) // 1 hour
                .signWith(Keys.hmacShaKeyFor(key), SignatureAlgorithm.HS256)
                .compact();

        return TokenResponse.builder()
                .token("Bearer " + accessToken)
                .build();
    }

    /**
     * true or throw
     */
    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().setSigningKey(refreshKey).parseClaimsJws(token).getBody();
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "이상한 JWT Token");
        } catch (ExpiredJwtException e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "만료된 JWT Token");
        } catch (UnsupportedJwtException e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "지원하지 않는 JWT Token");
        } catch (IllegalArgumentException e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "JWT claims이 텅텅 비었어요.");
        }
    }

    // 토큰에 이메일 저장함
    public String getRefreshSubFromToken(String token) {
        String email = getRefreshAllClaims(token).getSubject();
        return email;
    }

    private Claims getRefreshAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(refreshKey)
                .parseClaimsJws(token)
                .getBody();
    }
}