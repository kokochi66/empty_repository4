package com.memorial.st.mst.controller.content;

import com.memorial.st.mst.controller.content.io.IoAnime;
import com.memorial.st.mst.domain.content.enumType.SeasonType;
import com.memorial.st.mst.domain.content.model.MstAnime;
import com.memorial.st.mst.service.content.AnimeService;
import com.memorial.st.mst.service.content.repository.AnimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping(value = "/anime")
public class AnimeController {

    @Autowired
    private AnimeService animeService;

    @GetMapping("/season/list")
    public List<SeasonType> animeSeasonList() {
        return Arrays.asList(SeasonType.values());
    }

    @GetMapping("/list/{season}")
    public List<IoAnime> animeListBySeason(@PathVariable("season") SeasonType season) {
        log.info("/anime/list/{}", season);
        return animeService.getAnimeListBySeason(season).stream().map(IoAnime::convert).collect(Collectors.toList());
    }

    @PostMapping("/insert")
    public void addNewAnime(MstAnime anime) throws IOException {
        log.info("/anime/insert - " + anime);
        animeService.upsertAnime(anime);
    }
}
