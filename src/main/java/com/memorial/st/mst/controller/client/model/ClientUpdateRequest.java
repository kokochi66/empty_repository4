package com.memorial.st.mst.controller.client.model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientUpdateRequest {

    private Set<String> clientAuthenticationMethods;
    private Set<String> authorizationGrantTypes;
    private Set<String> scopes;
    private Set<String> redirectUris;
}
