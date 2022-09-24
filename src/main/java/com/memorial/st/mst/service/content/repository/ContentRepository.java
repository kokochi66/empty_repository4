package com.memorial.st.mst.service.content.repository;

import com.memorial.st.mst.domain.content.MstContent;
import com.memorial.st.mst.domain.content.model.MstMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ContentRepository extends JpaRepository<MstContent, Long> {
}
