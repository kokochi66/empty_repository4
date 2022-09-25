package com.memorial.st.mst.Service;

import com.memorial.st.mst.domain.content.MstSeason;
import com.memorial.st.mst.domain.content.enumType.SeasonType;
import com.memorial.st.mst.domain.content.model.MstAnime;
import com.memorial.st.mst.service.content.AnimeService;
import com.memorial.st.mst.service.content.repository.AnimeRepository;
import com.memorial.st.mst.service.content.repository.SeasonRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@SpringBootTest
@AutoConfigureMockMvc
public class AnimeTest {

    @Autowired
    private AnimeRepository animeRepository;
    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private AnimeService animeService;

    @Test
    @Transactional
    @Rollback(false)
    void test() throws Exception {
        List<MstAnime> animeListBySeason = animeService.getAnimeListBySeason(SeasonType.WINTER_2022);
        for (MstAnime anime : animeListBySeason) {
            System.out.println("[" +anime.getTitle() +"] "+
                    anime.getSeasonList().stream().findAny().orElse(null).getSeason());
        }
    }
}
