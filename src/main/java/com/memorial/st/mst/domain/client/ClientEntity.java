package com.memorial.st.mst.domain.client;

import lombok.*;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "mst_client_entity")
public class ClientEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false, unique = true)
    private String clientId;        // 인덱스 설정 필요

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String clientSecret;

    @OneToMany(mappedBy = "clientEntity", cascade = CascadeType.ALL)
    private Set<ClientAuthenticationMethodEntity> clientAuthenticationMethods;

    @OneToMany(mappedBy = "clientEntity", cascade = CascadeType.ALL)
    private Set<AuthorizationGrantTypeEntity> authorizationGrantTypes;

    @OneToMany(mappedBy = "clientEntity", cascade = CascadeType.ALL)
    private Set<ScopeEntity> scopes;

    @OneToMany(mappedBy = "clientEntity", cascade = CascadeType.ALL)
    private Set<RedirectUriEntity> redirectUris;


    public static ClientEntity fromClientEntity(RegisteredClient registeredClient) {
        return ClientEntity.builder()
                .id(registeredClient.getId())
                .clientId(registeredClient.getClientId())
                .clientSecret(registeredClient.getClientSecret())
                .clientAuthenticationMethods(registeredClient.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethodEntity::fromClientAuthenticationMethod).collect(Collectors.toSet()))
                .authorizationGrantTypes(registeredClient.getAuthorizationGrantTypes().stream().map(AuthorizationGrantTypeEntity::fromAuthorizationGrantType).collect(Collectors.toSet()))
                .scopes(registeredClient.getScopes().stream().map(ScopeEntity::fromScope).collect(Collectors.toSet()))
                .redirectUris(registeredClient.getRedirectUris().stream().map(RedirectUriEntity::fromRedirectUri).collect(Collectors.toSet()))
                .build();
    }

    public RegisteredClient toRegisteredClient() {
        return RegisteredClient.withId(getId())
                .clientId(getClientId())
                .clientSecret(getClientSecret())
                .clientAuthenticationMethods(clientAuthenticationMethods -> clientAuthenticationMethods.addAll(
                        getClientAuthenticationMethods().stream()
                                .map(ClientAuthenticationMethodEntity::toClientAuthenticationMethod)
                                .collect(Collectors.toSet())))
                .authorizationGrantTypes(authorizationGrantTypes -> authorizationGrantTypes.addAll(
                        getAuthorizationGrantTypes().stream()
                                .map(AuthorizationGrantTypeEntity::toAuthorizationGrantType)
                                .collect(Collectors.toSet())))
                .redirectUris(redirectUris -> redirectUris.addAll(getRedirectUris().stream()
                        .map(RedirectUriEntity::toRedirectUri)
                        .collect(Collectors.toSet())))
                .scopes(scopes -> scopes.addAll(
                        getScopes().stream()
                                .map(ScopeEntity::toScope)
                                .collect(Collectors.toSet())))
                .build();
    }

    // Getters, Setters, and other utility methods
}
