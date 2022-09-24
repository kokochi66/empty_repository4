package com.memorial.st.mst.service.content;

import com.memorial.st.mst.domain.content.enumType.ImageType;
import com.memorial.st.mst.domain.content.enumType.SeasonType;
import com.memorial.st.mst.domain.content.model.MstAnime;
import com.memorial.st.mst.domain.content.model.MstImage;
import com.memorial.st.mst.service.content.repository.AnimeRepository;
import com.memorial.st.mst.service.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
public class AnimeService {

    @Autowired
    private AnimeRepository animeRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private FileService fileService;

    public List<MstAnime> getAnimeList() {
        return animeRepository.findAll();
    }

    public List<MstAnime> getAnimeListBySeason(SeasonType seasonType) {
        return animeRepository.findAllBySeason(seasonType);
    }

    public MstAnime getAnimeById(Long id) {
        return animeRepository.getById(id);
    }

    @Transactional
    public void upsertAnime(MstAnime anime) {
        animeRepository.save(anime);
        log.info("TEST : : anime saved = " + anime.getTitle());
        log.info("TEST : : anime getKeyVisuals = " + (anime.getKeyVisuals() != null));
        if(anime.getKeyVisuals() != null) {
            log.info("TEST : : anime getKeyVisuals Size = " + anime.getKeyVisuals().size());
            List<MultipartFile> keyVisuals = anime.getKeyVisuals();
            for (MultipartFile keyVisualFile : keyVisuals) {
                log.info("TEST :: animeKeyVisual = " + keyVisualFile.getOriginalFilename());
                MstImage image = new MstImage();
                image.setParent(anime);
                image.setDescription(anime.getTitle() + "_keyVisual");
                image.setImageType(ImageType.KEY_VISUAL);
                image.setSavedFile(keyVisualFile);
                imageService.upsertImage(image);
            }

        }
    }
}
