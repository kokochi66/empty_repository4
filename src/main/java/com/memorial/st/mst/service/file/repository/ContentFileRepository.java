package com.memorial.st.mst.service.file.repository;

import com.memorial.st.mst.domain.file.ContentFile;
import com.memorial.st.mst.domain.file.ContentFilePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ContentFileRepository extends JpaRepository<ContentFile, ContentFilePK> {

    List<ContentFile> findAllByContentId(Long contentId);
}
