package com.memorial.st.mst.domain.client;

import lombok.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mst_authorization_grant_type")
public class AuthorizationGrantTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String grantType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_entity_id")
    private ClientEntity clientEntity;

    public AuthorizationGrantType toAuthorizationGrantType() {
        return new AuthorizationGrantType(grantType);
    }

    public static AuthorizationGrantTypeEntity fromAuthorizationGrantType(AuthorizationGrantType authorizationGrantType) {
        return AuthorizationGrantTypeEntity.builder()
                .grantType(authorizationGrantType.getValue())
                .build();
    }
}
