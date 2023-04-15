package com.memorial.st.mst.domain.user.service.repository;

import com.memorial.st.mst.domain.user.MstUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface MstUserRepository extends JpaRepository<MstUser, Long> {
}
