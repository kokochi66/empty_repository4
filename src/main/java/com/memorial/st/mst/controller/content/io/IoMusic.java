package com.memorial.st.mst.controller.content.io;

import com.memorial.st.mst.domain.content.MstContent;
import com.memorial.st.mst.domain.content.MstHighlight;
import com.memorial.st.mst.domain.content.MstSeason;
import com.memorial.st.mst.domain.content.enumType.MusicType;
import com.memorial.st.mst.domain.content.enumType.SeasonType;
import com.memorial.st.mst.domain.content.model.MstAnime;
import com.memorial.st.mst.domain.content.model.MstMusic;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
public class IoMusic {

    private Long id;
    private String fileSrc;
    private Integer point;

    private String title;
    private String vocal;
    private Float volume;
    private MusicType musicType;
    private List<Integer> hightlightList = new ArrayList<>();

    private LocalDateTime regDate;
    private Long parentId;
    private List<Long> childList = new ArrayList<>();

    private MultipartFile savedFile;

    public static IoMusic convert(MstMusic music) {
        IoMusic.IoMusicBuilder builder = IoMusic.builder()
                .id(music.getId())
                .fileSrc(music.getFileSrc())
                .point(music.getPoint())
                .title(music.getTitle())
                .vocal(music.getVocal())
                .volume(music.getVolume())
                .musicType(music.getMusicType())
                .regDate(music.getRegDate());

        if (music.getHighlightList() != null) {
            builder.hightlightList(music.getHighlightList().stream().map(MstHighlight::getTime).collect(Collectors.toList()));
        }

        if (music.getParent() != null) {
            builder.parentId(music.getParent().getId());
        }

        if (music.getChild() != null) {
            builder.childList(music.getChild().stream().map(MstContent::getId).collect(Collectors.toList()));
        }
        return builder.build();
    }

    public MstMusic toEntity() {
        MstMusic music = new MstMusic();
        music.setId(this.id);
        music.setFileSrc(this.fileSrc);
        music.setPoint(this.point);
        music.setTitle(this.title);
        music.setVocal(this.vocal);
        music.setVolume(this.volume);
        music.setMusicType(this.musicType);
        music.setRegDate(this.regDate);
        return music;
    }

}
