package com.memorial.st.mst.domain.client.service.repository;

import com.memorial.st.mst.domain.client.ClientAuthenticationMethodEntity;
import com.memorial.st.mst.domain.client.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ClientAuthenticationMethodEntityRepository extends JpaRepository<ClientAuthenticationMethodEntity, Long> {
    Set<ClientAuthenticationMethodEntity> findAllByClientEntity(ClientEntity clientEntity);
}
