package com.memorial.st.mst.controller.user.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserConsentRequest {

    private Long userId;
    private String clientEntityId;
    private List<String> scopes;
}
