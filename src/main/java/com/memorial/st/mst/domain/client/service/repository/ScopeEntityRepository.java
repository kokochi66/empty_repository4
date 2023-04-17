package com.memorial.st.mst.domain.client.service.repository;

import com.memorial.st.mst.domain.client.ClientEntity;
import com.memorial.st.mst.domain.client.ScopeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ScopeEntityRepository extends JpaRepository<ScopeEntity, Long> {
    Set<ScopeEntity> findAllByClientEntity(ClientEntity clientEntity);
}
