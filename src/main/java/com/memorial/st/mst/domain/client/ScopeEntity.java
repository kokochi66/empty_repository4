package com.memorial.st.mst.domain.client;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "mst_scope_entity")
public class ScopeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String scope;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_entity_id")
    private ClientEntity clientEntity;

    public static ScopeEntity fromScope(String scope) {
        return ScopeEntity.builder()
                .scope(scope)
                .build();
    }

    public String toScope() {
        return getScope();
    }
}
