package com.memorial.st.mst.controller.client.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientRegistrationRequest {
    private String clientName;
    private List<String> redirectUris;
}
