package com.finalmelontoken.fmtauthserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalmelontoken.fmtauthserver.domain.GoogleOAuthTokenDto;
import com.finalmelontoken.fmtauthserver.domain.GoogleUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class GoogleOAuthProvider {
    private final String googleLoginUrl = "https://accounts.google.com/o/oauth2/v2/auth";
    private final String GOOGLE_TOKEN_REQUEST_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${app.google.clientId}")
    private String googleClientId = "493607296742-32i93sr1sh4c2ijkbhrg1hrbvnmfaoea.apps.googleusercontent.com";
    @Value("${app.google.redirect}")
    private String googleRedirectUrl = "http://localhost:8080/login/oauth2/code/google";
    @Value("${app.google.secret}")
    private String googleClientSecret = "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";
    public String getOauthRedirectURL() {
        return googleLoginUrl +
                "?client_id=" + googleClientId +
                "&redirect_uri=" + googleRedirectUrl +
                "&scope=profile email" +
                "&response_type=code";
    }

    public ResponseEntity<String> requestAccessToken(String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", googleClientId);
        params.put("client_secret", googleClientSecret);
        params.put("redirect_uri", googleRedirectUrl);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL, params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        return null;
    }

    public GoogleOAuthTokenDto getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        System.out.println("response,getOBody" + response.getBody());
        GoogleOAuthTokenDto googleOAuthTokenDto = objectMapper.readValue(response.getBody(), GoogleOAuthTokenDto.class);
        return googleOAuthTokenDto;
    }

    public ResponseEntity<String> requestUserInfo(GoogleOAuthTokenDto oAuthTokenDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthTokenDto.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
        System.out.println(response.getBody());
        return response;
    }

    public GoogleUserInfoDto getUserInfo(ResponseEntity<String> response) throws JsonProcessingException {
        GoogleUserInfoDto googleUserInfoDto = objectMapper.readValue(response.getBody(), GoogleUserInfoDto.class);
        return googleUserInfoDto;
    }
}