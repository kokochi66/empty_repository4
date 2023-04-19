package com.memorial.st.mst.domain.client.service;

import com.memorial.st.mst.domain.client.*;
import com.memorial.st.mst.domain.client.service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DBRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientEntityRepository clientEntityRepository;
    private final AuthorizationGrantTypeEntityRepository authorizationGrantTypeEntityRepository;
    private final ClientAuthenticationMethodEntityRepository clientAuthenticationMethodEntityRepository;
    private final RedirectUriEntityRepository redirectUriEntityRepository;
    private final ScopeEntityRepository scopeEntityRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
        // Save the RegisteredClient to the database\
        clientEntityRepository.save(ClientEntity.fromClientEntity(registeredClient));
        authorizationGrantTypeEntityRepository.saveAll(registeredClient.getAuthorizationGrantTypes().stream()
                        .map(AuthorizationGrantTypeEntity::fromAuthorizationGrantType).collect(Collectors.toSet()));
        clientAuthenticationMethodEntityRepository.saveAll(registeredClient.getClientAuthenticationMethods().stream()
                        .map(ClientAuthenticationMethodEntity::fromClientAuthenticationMethod).collect(Collectors.toSet()));
        redirectUriEntityRepository.saveAll(registeredClient.getRedirectUris().stream()
                        .map(RedirectUriEntity::fromRedirectUri).collect(Collectors.toSet()));
        scopeEntityRepository.saveAll(registeredClient.getScopes().stream()
                        .map(ScopeEntity::fromScope).collect(Collectors.toSet()));
    }

    @Override
    public RegisteredClient findById(String id) {
        return optional(clientEntityRepository.findByIdWithFetchJoin(id));
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return optional(clientEntityRepository.findByClientIdWithFetchJoin(clientId));
    }

    private RegisteredClient optional(Optional<ClientEntity> clientEntityOptional) {
        if (clientEntityOptional.isEmpty()) return null;
        return clientEntityOptional.get().toRegisteredClient();
    }
}
