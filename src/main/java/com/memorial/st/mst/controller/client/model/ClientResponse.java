package com.memorial.st.mst.controller.client.model;

import com.memorial.st.mst.domain.client.*;
import lombok.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponse {

    private String id;
    private String clientId;        // 인덱스 설정 필요
    private String clientName;
    private Set<String> clientAuthenticationMethods;
    private Set<String> authorizationGrantTypes;
    private Set<String> scopes;
    private Set<String> redirectUris;

    public ClientResponse(ClientEntity clientEntity) {
        Optional.ofNullable(clientEntity.getId()).ifPresent(v -> setId(clientEntity.getId()));
        Optional.ofNullable(clientEntity.getClientId()).ifPresent(v -> setClientId(clientEntity.getClientId()));
        Optional.ofNullable(clientEntity.getClientName()).ifPresent(v -> setClientName(clientEntity.getClientName()));
        Optional.ofNullable(clientEntity.getClientAuthenticationMethods()).ifPresent(v -> setClientAuthenticationMethods(v.stream().map(ClientAuthenticationMethodEntity::getMethod).collect(Collectors.toSet())));
        Optional.ofNullable(clientEntity.getAuthorizationGrantTypes()).ifPresent(v -> setAuthorizationGrantTypes(v.stream().map(AuthorizationGrantTypeEntity::getGrantType).collect(Collectors.toSet())));
        Optional.ofNullable(clientEntity.getScopes()).ifPresent(v -> setScopes(v.stream().map(ScopeEntity::getScope).collect(Collectors.toSet())));
        Optional.ofNullable(clientEntity.getRedirectUris()).ifPresent(v -> setRedirectUris(v.stream().map(RedirectUriEntity::getRedirectUri).collect(Collectors.toSet())));
    }
}
