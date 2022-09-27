package com.memorial.st.mst.controller.content.io;

import com.memorial.st.mst.domain.content.MstContent;
import com.memorial.st.mst.domain.content.MstSeason;
import com.memorial.st.mst.domain.content.enumType.SeasonType;
import com.memorial.st.mst.domain.content.model.MstAnime;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class IoAnime {

    private Long id;
    private String fileSrc;
    private Integer point;
    private String title;
    private String director;
    private List<SeasonType> seasonList = new ArrayList<>();

    private LocalDateTime regDate;
    private Long parentId;
    private List<Long> childList = new ArrayList<>();


    public static IoAnime convert(MstAnime anime) {
        IoAnimeBuilder builder = IoAnime.builder()
                .id(anime.getId())
                .fileSrc(anime.getFileSrc())
                .point(anime.getPoint())
                .title(anime.getTitle())
                .director(anime.getDirector())
                .seasonList(anime.getSeasonList().stream().map(MstSeason::getSeason).collect(Collectors.toList()))
                .regDate(anime.getRegDate());

        if (anime.getParent() != null) {
            builder.parentId(anime.getParent().getId());
        }

        if (anime.getChild() != null) {
            builder.childList(anime.getChild().stream().map(MstContent::getId).collect(Collectors.toList()));
        }
        return builder.build();
    }

}
