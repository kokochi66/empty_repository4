package com.memorial.st.mst.controller.content;

import com.memorial.st.mst.domain.content.model.MstMusic;
import com.memorial.st.mst.service.file.FileService;
import com.memorial.st.mst.service.content.MusicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping(value = "/music")
public class MusicController {

    @Autowired
    private FileService fileService;
    @Autowired
    private MusicService musicService;


    @PostMapping("/insert")
    public String addNewMusic(MstMusic music) throws IOException {
        log.info("/music/insert - " + music);

        music.setPoint(0);
        music.setRegDate(LocalDateTime.now());
        music.setVolume(0.5F);
        musicService.upsertMusic(music);

//        if (!music.getMusicFile().isEmpty()) {
//            fileService.save(music.getMusicFile());
//        }
        return "redirect:/";
    }
}
