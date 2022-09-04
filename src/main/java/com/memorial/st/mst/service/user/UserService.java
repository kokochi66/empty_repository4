package com.memorial.st.mst.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memorial.st.mst.domain.user.MstUser;
import com.memorial.st.mst.service.user.repository.MstUserRepository;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

@Service
@Slf4j
public class UserService {

    @Autowired
    private MstUserRepository mstUserRepository;

    private final static String encryptKey = "y2Q7JKDeQXT25u2e";
    private final static int GCM_IV_LENGTH = 12;

    public MstUser getUser(Long userId) throws Exception {
        return mstUserRepository.findById(userId).orElseThrow(() -> new Exception("Not Found User"));
    }

    // 로그인
    public String userLogin(Long userId) throws Exception {
        MstUser user = getUser(userId);
        SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(), "AES");
        String iv = RandomString.make(GCM_IV_LENGTH);
        byte[] encrypt = encrypt(("{userId:" + user.getUserId() + ", password:" + user.getPassword() + ", role:" + user.getRole()+"}"), secretKey, iv);
        log.info("TEST :: encrypt = {}", encrypt.toString());
        return encrypt.toString();
    }

    public MstUser getUserCookie(String plainText) throws Exception {
        SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(), "AES");
        String decrypt = decrypt(plainText.getBytes(), secretKey);
        log.info("TEST :: decrpyt {}", decrypt);
        ObjectMapper objectMapper = new ObjectMapper();
        MstUser mstUser = objectMapper.readValue(decrypt, MstUser.class);
        log.info("TEST :: mstUser = {}", mstUser);
        return mstUser;
    }

    public MstUser register(MstUser user) throws Exception {
        user.setModDate(LocalDateTime.now());
        return mstUserRepository.save(user);
    }

    public static void main(String[] args) throws Exception{
        SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(), "AES");
        byte[] encrypt = encrypt("testasdasdasdasdasd", secretKey, "abc");
        System.out.println("TEST :: encrypt = " + encrypt.toString());
        String decrypt = decrypt(encrypt, secretKey);
        System.out.println("TEST :: decrypt = " + decrypt);
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
        AlgorithmParameterSpec gcmIv = new GCMParameterSpec(128, cipherMessage, 0, GCM_IV_LENGTH);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmIv);

        //use everything from 12 bytes on as ciphertext
        byte[] plainText = cipher.doFinal(cipherMessage, GCM_IV_LENGTH, cipherMessage.length - GCM_IV_LENGTH);

        return new String(plainText, StandardCharsets.UTF_8);
    }
}
