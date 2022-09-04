package com.memorial.st.mst.service.user.repository;

import com.memorial.st.mst.domain.music.Music;
import com.memorial.st.mst.domain.user.MstUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface MstUserRepository extends JpaRepository<MstUser, Long> {
}
