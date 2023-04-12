package com.memorial.st.mst.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AuthJwtInterceptor implements HandlerInterceptor {

    @Value("${auth.encrypt.key}")
    private String encrpytKey;

    @Value("${auth.project.id}")
    private String projectId;

    private final ConcurrentHashMap<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> blockedClients = new ConcurrentHashMap<>();

    private RateLimiter getRateLimiter(String clientIp) {
        return rateLimiters.computeIfAbsent(clientIp, k -> RateLimiter.create(10.0)); // 초당 50회 요청
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String clientIp = request.getRemoteAddr(); // 클라이언트 IP 가져오기
        RateLimiter rateLimiter = getRateLimiter(clientIp);

        if (blockedClients.containsKey(clientIp)) {
            LocalDateTime blockedUntil = blockedClients.get(clientIp);
            if (LocalDateTime.now().isBefore(blockedUntil)) {
                // 차단 기간 동안 요청을 거부합니다.
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                throw new Exception("로그인 요청이 너무 많습니다. 잠시 후 다시 시도해주세요.");
            } else {
                // 차단 기간이 끝났으면, 차단 목록에서 제거합니다.
                blockedClients.remove(clientIp);
            }
        }

        if (!rateLimiter.tryAcquire()) {
            // 요청 속도가 제한을 초과했을 때 차단 목록에 추가하고 적절한 응답을 반환합니다.
            blockedClients.put(clientIp, LocalDateTime.now().plusMinutes(10));
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            throw new Exception("로그인 요청이 너무 많습니다. 잠시 후 다시 시도해주세요.");
        }

        // @AuthExcludes 어노테이션 확인
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AuthExcludes authExcludes = handlerMethod.getMethodAnnotation(AuthExcludes.class);
            if (authExcludes != null) {
                // @AuthExcludes 어노테이션이 있으면 인터셉터를 건너뛰기
                return true;
            }
        }

        // Authorization 헤더에서 JWT 토큰 찾기
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증되지 않은 요청입니다.");
            return false;
        }

        String jwtToken = authorizationHeader.substring(7); // "Bearer "를 제외한 나머지 부분

        try {
            // JWT 토큰 검증
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(encrpytKey))
                    .withIssuer(projectId)
                    .build()
                    .verify(jwtToken);

            // 사용자 ID와 역할을 request attribute에 저장
            request.setAttribute("userId", decodedJWT.getClaim("userId").asLong());
            request.setAttribute("role", decodedJWT.getClaim("role").asString());
        } catch (JWTVerificationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증되지 않은 요청입니다.");
            return false;
        }

        return true;
    }

}
