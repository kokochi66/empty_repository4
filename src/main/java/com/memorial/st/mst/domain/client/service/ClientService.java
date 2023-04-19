package com.memorial.st.mst.domain.client.service;

import com.memorial.st.mst.domain.client.ClientEntity;
import com.memorial.st.mst.domain.client.service.repository.ClientEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

@Service
public class ClientService {

    private static final SecureRandom random = new SecureRandom();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Autowired
    private ClientEntityRepository clientEntityRepository;

    public ClientEntity registerClient(String clientName) {
        return clientEntityRepository.save(ClientEntity.builder()
                .id(generateRegisteredClientId(clientName))
                .clientId(generateClientId())
                .clientSecret(generateClientSecret())
                .clientName(clientName)
                .build());
    }

    public static String generateClientId() {
        return UUID.randomUUID().toString();
    }

    public static String generateClientSecret() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String generateRegisteredClientId(String clientName) {
        return clientName + "_" + LocalDateTime.now().format(formatter) + "_" + UUID.randomUUID();
    }
}
