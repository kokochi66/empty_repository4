package com.memorial.st.mst.controller.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.memorial.st.mst.controller.user.model.UserConsentRequest;
import com.memorial.st.mst.domain.user.MstRole;
import com.memorial.st.mst.domain.user.MstUser;
import com.memorial.st.mst.domain.user.service.UserService;
import com.memorial.st.mst.domain.user.service.repository.MstUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private UserService userService;
    @Autowired
    private MstUserRepository userRepository;

    private MstUser testUser;

    @Value("${auth.encrypt.key}")
    private String encrpytKey;

    @Value("${auth.project.id}")
    private String projectId;

    @Autowired
    private TestRestTemplate restTemplate;

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
        String jwtToken = userService.userLogin(userId, testUser.getPassword());

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

    @Test
    public void createConsentTest() {
        // 테스트 데이터 생성
        UserConsentRequest consentRequest = new UserConsentRequest();
        consentRequest.setUserId(1L);
        consentRequest.setClientEntityId("test-client-id");
        List<String> scopes = Arrays.asList("read", "write");
        consentRequest.setScopes(scopes);

        // 사용자 정보 동의 요청 테스트
        ResponseEntity<Boolean> response = restTemplate.postForEntity("/user/consent", consentRequest, Boolean.class);

        // 응답 상태 및 본문 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(true);

        // 동의 내역 확인 (예: 데이터베이스에서 조회)
        // 이 부분은 서비스에서 동의 정보를 어떻게 저장하고 검색하는지에 따라 구현이 달라집니다.
        // List<String> consentScopes = userConsentService.findConsentScopes(consentRequest.getUserId(), consentRequest.getClientId());
        // assertThat(consentScopes).containsAll(scopes);
    }
}
