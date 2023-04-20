package com.memorial.st.mst.domain.client.service;

import com.memorial.st.mst.controller.client.model.ClientUpdateRequest;
import com.memorial.st.mst.domain.client.*;
import com.memorial.st.mst.domain.client.service.repository.ClientEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public ClientEntity updateClient(String clientId, ClientUpdateRequest request) {
        Optional<ClientEntity> clientOptional = clientEntityRepository.findByClientIdWithFetchJoin(clientId);
        if (clientOptional.isPresent()) {
            ClientEntity clientEntity = clientOptional.get();
            Optional.ofNullable(request.getClientAuthenticationMethods())
                    .ifPresent(s -> clientEntity.setClientAuthenticationMethods(s.stream().map(v -> ClientAuthenticationMethodEntity.builder().method(v).clientEntity(clientEntity).build()).collect(Collectors.toSet())));
            Optional.ofNullable(request.getAuthorizationGrantTypes())
                    .ifPresent(s -> clientEntity.setAuthorizationGrantTypes(s.stream().map(v -> AuthorizationGrantTypeEntity.builder().grantType(v).clientEntity(clientEntity).build()).collect(Collectors.toSet())));
            Optional.ofNullable(request.getScopes())
                    .ifPresent(s -> clientEntity.setScopes(s.stream().map(v -> ScopeEntity.builder().scope(v).clientEntity(clientEntity).build()).collect(Collectors.toSet())));
            Optional.ofNullable(request.getRedirectUris())
                    .ifPresent(s -> clientEntity.setRedirectUris(s.stream().map(v -> RedirectUriEntity.builder().redirectUri(v).clientEntity(clientEntity).build()).collect(Collectors.toSet())));

            return clientEntityRepository.save(clientEntity);
        } else {
            return null;
        }
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
