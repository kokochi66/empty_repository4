package com.memorial.st.mst.domain.client.service.repository;

import com.memorial.st.mst.domain.client.ClientEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientEntityRepository extends JpaRepository<ClientEntity, Long> {

    @EntityGraph(attributePaths = {"clientAuthenticationMethods", "authorizationGrantTypes", "scopes", "redirectUris"})
    @Query("SELECT c FROM ClientEntity c WHERE c.id = :id")
    Optional<ClientEntity> findByIdWithFetchJoin(String id);

    @EntityGraph(attributePaths = {"clientAuthenticationMethods", "authorizationGrantTypes", "scopes", "redirectUris"})
    @Query("SELECT c FROM ClientEntity c WHERE c.clientId = :clientId")
    Optional<ClientEntity> findByClientIdWithFetchJoin(String clientId);
}
