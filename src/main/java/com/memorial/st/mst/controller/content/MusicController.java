package com.memorial.st.mst.controller.content;

import com.memorial.st.mst.domain.content.enumType.MusicType;
import com.memorial.st.mst.domain.content.enumType.SeasonType;
import com.memorial.st.mst.domain.content.model.MstMusic;
import com.memorial.st.mst.service.file.FileService;
import com.memorial.st.mst.service.content.MusicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/music")
public class MusicController {

    @Autowired
    private FileService fileService;
    @Autowired
    private MusicService musicService;

    @GetMapping("/type/list")
    public List<MusicType> animeSeasonList() {
        return Arrays.asList(MusicType.values());
    }
}
