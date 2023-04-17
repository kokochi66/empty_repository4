package com.memorial.st.mst.controller.oauth.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AccessTokenResponse {
    private String access_token;
    private int expires_in;
    private String token_type;
    private String scope;
}
