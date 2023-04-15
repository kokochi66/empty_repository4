package com.memorial.st.mst.domain.client.service;

import com.memorial.st.mst.domain.client.ClientEntity;
import com.memorial.st.mst.domain.client.RedirectUriEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RedirectUriEntityRepository extends JpaRepository<RedirectUriEntity, Long> {
    Set<RedirectUriEntity> findAllByClientEntity(ClientEntity clientEntity);
}
