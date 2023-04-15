package com.memorial.st.mst.domain.client;

import lombok.*;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import javax.persistence.*;

@Entity
@Table(name = "mst_client_authentication_method")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientAuthenticationMethodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String method;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_entity_id")
    private ClientEntity clientEntity;

    public ClientAuthenticationMethod toClientAuthenticationMethod() {
        return new ClientAuthenticationMethod(method);
    }

    public static ClientAuthenticationMethodEntity fromClientAuthenticationMethod(ClientAuthenticationMethod clientAuthenticationMethod) {
        return ClientAuthenticationMethodEntity.builder()
                .method(clientAuthenticationMethod.getValue())
                .build();
    }
}
