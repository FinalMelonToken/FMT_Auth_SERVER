package com.finalmelontoken.fmtauthserver.util;

import com.finalmelontoken.fmtauthserver.domain.TokenInfo;
import com.finalmelontoken.fmtauthserver.exception.GlobalException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private final Key accessKey;
    private final Key refreshKey;

    public JwtTokenProvider(@Value("${jwt.access.secret}") String accessSecretKey,
                            @Value("${jwt.refresh.secret}") String refreshSecretKey) {
        byte[] accessKeyBytes = Decoders.BASE64.decode(accessSecretKey);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecretKey);
        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    public TokenInfo generateToken(String email) {

        long now = (new Date()).getTime();
        String accessToken = Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(now + (60000 * 60))) // 1 hour
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(now + (60000 * 60 * 24 * 7))) // 1 week
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .accessToken("Bearer " + accessToken)
                .refreshToken("Bearer " + refreshToken)
                .build();
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().setSigningKey(refreshKey).parseClaimsJws(token).getBody();
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
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