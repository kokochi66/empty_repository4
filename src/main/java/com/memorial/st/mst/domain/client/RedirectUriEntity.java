package com.memorial.st.mst.domain.client;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "mst_redirect_uri")
public class RedirectUriEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String redirectUri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_entity_id")
    private ClientEntity clientEntity;

    public static RedirectUriEntity fromRedirectUri(String redirectUri) {
        return RedirectUriEntity.builder()
                .redirectUri(redirectUri)
                .build();
    }

    public String toRedirectUri() {
        return getRedirectUri();
    }
}
