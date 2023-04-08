package com.memorial.st.mst.controller.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.memorial.st.mst.domain.user.MstRole;
import com.memorial.st.mst.domain.user.MstUser;
import com.memorial.st.mst.service.user.UserService;
import com.memorial.st.mst.service.user.repository.MstUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;
    @Autowired
    private MstUserRepository userRepository;

    private MstUser testUser;

    @Value("${auth.encrypt.key}")
    private String encrpytKey;

    @Value("${auth.project.id}")
    private String projectId;

    @BeforeEach
    public void setUp() {

        testUser = MstUser.builder()
                .userName("testUser")
                .password("testPassword")
                .role(MstRole.ROLE_USER)
                .build();
        userRepository.save(testUser);
    }

    @AfterEach
    public void tearDown() {
        // 테스트용 사용자 삭제
        userRepository.delete(testUser);
    }

    @Test
    public void testUserLogin() throws Exception {
        // 임의의 사용자 ID로 로그인 테스트
        Long userId = testUser.getUserId();
        String jwtToken = userService.userLogin(userId);

        // JWT 토큰이 비어있지 않은지 확인
        assertNotNull(jwtToken);

        // JWT 토큰 해독
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(encrpytKey))
                .withIssuer(projectId)
                .build()
                .verify(jwtToken);

        // 토큰에서 사용자 정보 확인
        assertEquals(userId, decodedJWT.getClaim("userId").asLong());

        // 토큰 만료 시간 확인 (1시간 이내)
        assertTrue(decodedJWT.getExpiresAt().after(new Date(System.currentTimeMillis())));
    }
}
