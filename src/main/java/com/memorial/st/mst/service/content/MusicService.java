package com.memorial.st.mst.service.content;

import com.memorial.st.mst.domain.content.MstContent;
import com.memorial.st.mst.domain.content.model.MstMusic;
import com.memorial.st.mst.service.content.repository.ContentRepository;
import com.memorial.st.mst.service.file.FileService;
import com.memorial.st.mst.service.content.repository.MusicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class MusicService {

    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private FileService fileService;

    public List<MstMusic> getMusicList() {
        return musicRepository.findAll();
    }

    public MstMusic getMusicByMusicId(Long musicId) {
        return musicRepository.getById(musicId);
    }

    @Transactional
    public void upsertMusic(MstMusic music) {
        if (music.getParentId() != null) {
            MstContent parentContent = contentRepository.findById(music.getParentId()).orElse(null);
            music.setParent(parentContent);
        }
        if (music.getSavedFile() != null) {
            music.setFileSrc(fileService.save(music.getSavedFile()));
        }
        musicRepository.save(music);
    }

    public void deleteMusicByMusicId(Long musicId) {
        musicRepository.deleteById(musicId);
    }
}
