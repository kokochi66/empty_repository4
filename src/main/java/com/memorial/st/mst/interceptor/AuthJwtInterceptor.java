package com.memorial.st.mst.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
public class AuthJwtInterceptor implements HandlerInterceptor {

    @Value("${auth.encrypt.key}")
    private String encrpytKey;

    @Value("${auth.project.id}")
    private String projectId;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // @AuthExcludes 어노테이션 확인
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AuthExcludes authExcludes = handlerMethod.getMethodAnnotation(AuthExcludes.class);
            if (authExcludes != null) {
                // @AuthExcludes 어노테이션이 있으면 인터셉터를 건너뛰기
                return true;
            }
        }

        // 쿠키에서 JWT 토큰 찾기
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증되지 않은 요청입니다.");
            return false;
        }

        Cookie jwtCookie = Arrays.stream(cookies)
                .filter(cookie -> "PRJ-MST-CENT-USER".equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        if (jwtCookie == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증되지 않은 요청입니다.");
            return false;
        }

        String jwtToken = jwtCookie.getValue();

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
