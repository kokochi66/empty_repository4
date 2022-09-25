package com.memorial.st.mst.service.content;

import com.memorial.st.mst.domain.content.MstSeason;
import com.memorial.st.mst.domain.content.enumType.ImageType;
import com.memorial.st.mst.domain.content.enumType.SeasonType;
import com.memorial.st.mst.domain.content.model.MstAnime;
import com.memorial.st.mst.domain.content.model.MstImage;
import com.memorial.st.mst.service.content.repository.AnimeRepository;
import com.memorial.st.mst.service.content.repository.SeasonRepository;
import com.memorial.st.mst.service.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnimeService {

    @Autowired
    private AnimeRepository animeRepository;
    @Autowired
    private SeasonRepository seasonRepository;
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
        if(anime.getKeyVisuals() != null) {
            List<MultipartFile> keyVisuals = anime.getKeyVisuals();
            for (MultipartFile keyVisualFile : keyVisuals) {
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
