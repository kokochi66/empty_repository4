package com.memorial.st.mst.domain.client.service.repository;

import com.memorial.st.mst.domain.client.ClientEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientEntityRepository extends JpaRepository<ClientEntity, Long> {

    @Deprecated
    Optional<ClientEntity> findByRegisteredClientId(String clientId);
    @Deprecated
    Optional<ClientEntity> findByClientId(String clientId);
    // 다른 정보를 함께 가져와야 하므로, ClientEntity만 가져오는 쿼리는 가능하면 삼가는 것이 좋음


    @EntityGraph(attributePaths = {"clientAuthenticationMethods", "authorizationGrantTypes", "scopes", "redirectUris"})
    @Query("SELECT c FROM ClientEntity c WHERE c.entityId = :entityId")
    Optional<ClientEntity> findByIdWithFetchJoin(Long entityId);

    @EntityGraph(attributePaths = {"clientAuthenticationMethods", "authorizationGrantTypes", "scopes", "redirectUris"})
    @Query("SELECT c FROM ClientEntity c WHERE c.registeredClientId = :registeredClientId")
    Optional<ClientEntity> findByRegisteredClientIdWithFetchJoin(String registeredClientId);

    @EntityGraph(attributePaths = {"clientAuthenticationMethods", "authorizationGrantTypes", "scopes", "redirectUris"})
    @Query("SELECT c FROM ClientEntity c WHERE c.clientId = :clientId")
    Optional<ClientEntity> findByClientIdWithFetchJoin(String clientId);
}
