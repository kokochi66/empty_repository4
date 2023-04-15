package com.memorial.st.mst.domain.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.memorial.st.mst.domain.user.MstRole;
import com.memorial.st.mst.domain.user.MstUser;
import com.memorial.st.mst.domain.user.service.repository.MstUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@Slf4j
public class UserService {

    @Autowired
    private MstUserRepository mstUserRepository;

    @Value("${auth.encrypt.key}")
    private String encrpytKey;

    @Value("${auth.project.id}")
    private String projectId;

    public MstUser getUser(Long userId) throws Exception {
        return mstUserRepository.findById(userId).orElseThrow(() -> new Exception("Not Found User"));
    }

    // 로그인
    public String userLogin(Long userId, String password) throws Exception {
        MstUser user = getUser(userId);
        if (!user.getPassword().equalsIgnoreCase(password)) {
            throw new Exception("비밀번호가 틀립니다.");
        }

        String jwtToken = JWT.create()
                .withIssuer(projectId)
                .withClaim("userId", user.getUserId())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 만료 시간 설정 (1시간)
                .sign(Algorithm.HMAC256(encrpytKey)); // 서명 알고리즘 및 비밀 키 설정
        // HS512 알고리즘을 사용하여 더 강력한 암호화 알고리즘을 사용하기
        log.info("TEST :: encrypt = {}", jwtToken);
        return jwtToken;
    }

    public MstUser getUserCookie(String plainText) throws Exception {
        SecretKey secretKey = new SecretKeySpec(encrpytKey.getBytes(), "AES");
        String decrypt = decrypt(plainText.getBytes(), secretKey);
        log.info("TEST :: decrpyt {}", decrypt);
        ObjectMapper objectMapper = new ObjectMapper();
        MstUser mstUser = objectMapper.readValue(decrypt, MstUser.class);
        log.info("TEST :: mstUser = {}", mstUser);
        return mstUser;
    }

    public MstUser register(String userName, String nickName, String password, MstRole role) throws Exception {
        return mstUserRepository.save(MstUser.builder()
                        .userName(userName)
                        .nickName(nickName)
                        .password(password)
                        .regDate(LocalDateTime.now())
                        .modDate(LocalDateTime.now())
                        .role(role).build());
    }

    private static byte[] encrypt(String plaintext, SecretKey secretKey, String iv) throws Exception {
//        log.info("TEST :: iv = {}", iv);
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv.getBytes()); //128 bit auth tag length
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

        byte[] cipherText = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length() + cipherText.length);
        byteBuffer.put(iv.getBytes());
        byteBuffer.put(cipherText);
        return byteBuffer.array();
    }

    private static String decrypt(byte[] cipherMessage, SecretKey secretKey) throws Exception {
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        //use first 12 bytes for iv
        AlgorithmParameterSpec gcmIv = new GCMParameterSpec(128, cipherMessage, 0, 12);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmIv);

        //use everything from 12 bytes on as ciphertext
        byte[] plainText = cipher.doFinal(cipherMessage, 12, cipherMessage.length - 12);

        return new String(plainText, StandardCharsets.UTF_8);
    }

    private static String generateRandomString(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
