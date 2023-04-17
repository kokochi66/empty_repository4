package com.memorial.st.mst.domain.client.service.repository;

import com.memorial.st.mst.domain.client.AuthorizationGrantTypeEntity;
import com.memorial.st.mst.domain.client.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AuthorizationGrantTypeEntityRepository extends JpaRepository<AuthorizationGrantTypeEntity, Long> {
    Set<AuthorizationGrantTypeEntity> findAllByClientEntity(ClientEntity clientEntity);
}
