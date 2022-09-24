package com.memorial.st.mst.service.content.repository;

import com.memorial.st.mst.domain.content.model.MstImage;
import com.memorial.st.mst.domain.content.model.MstMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ImageRepository extends JpaRepository<MstImage, Long> {
}
