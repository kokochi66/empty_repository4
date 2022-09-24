package com.memorial.st.mst.service.content;

import com.memorial.st.mst.domain.content.enumType.SeasonType;
import com.memorial.st.mst.domain.content.model.MstAnime;
import com.memorial.st.mst.domain.content.model.MstImage;
import com.memorial.st.mst.service.content.repository.AnimeRepository;
import com.memorial.st.mst.service.content.repository.ImageRepository;
import com.memorial.st.mst.service.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private FileService fileService;

    @Transactional
    public void upsertImage(MstImage image) {
        if(image.getSavedFile() != null) {
            image.setFileSrc(fileService.save(image.getSavedFile()));
        }
        imageRepository.save(image);
    }
}
