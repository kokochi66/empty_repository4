package com.memorial.st.mst.domain.client.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.memorial.st.mst.controller.oauth.model.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Service
public class OAuth2TokenService {

    @Value("${auth.encrypt.key}")
    private String encrpytKey;

    @Value("${auth.project.id}")
    private String projectId;

    @Autowired
    private DBRegisteredClientRepository registeredClientRepository;

    public AccessTokenResponse issueAccessToken(String grantType, String clientId, String clientSecret, String scope) throws Exception {
        RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null || !registeredClient.getClientSecret().equalsIgnoreCase(clientSecret)) {
            throw new Exception("clientSecret이 일치하지 않습니다.");
        }

        // JWT 토큰 생성 및 만료 시간 설정
        String accessToken = JWT.create()
                .withIssuer(projectId) // 발급자 설정
                .withClaim("clientId", clientId) // 클라이언트 ID
                .withClaim("scope", scope) // 스코프 설정
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 만료 시간 설정 (1시간)
                .sign(Algorithm.HMAC256(encrpytKey)); // 서명 알고리즘 및 비밀 키 설정

        int expiresIn = 3600; // 토큰 만료 시간 (초)
        String tokenType = "Bearer"; // 토큰 타입

        // 액세스 토큰 응답 객체 생성 및 반환
        return new AccessTokenResponse(accessToken, expiresIn, tokenType, scope);
    }

    // 인증 헤더값을 받아서, clientId와 clientSecret으로 변환
    public String[] decodeClientCredentials(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring("Basic ".length());
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
        return credentials.split(":", 2);
    }

}
