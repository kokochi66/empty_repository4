package com.memorial.st.mst.domain.content.model;

import com.memorial.st.mst.domain.content.MstContent;
import com.memorial.st.mst.domain.content.MstHighlight;
import com.memorial.st.mst.domain.content.enumType.ImageType;
import com.memorial.st.mst.domain.content.enumType.MusicType;
import com.memorial.st.mst.domain.content.enumType.SeasonType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MUSIC")
@Getter
@Setter
@Table(name = "mst_music")
public class MstMusic extends MstContent {

    private static final long serialVersionUID = 4627966143226861068L;
    private String title;
    private String vocal;
    private Float volume;

    @Enumerated(EnumType.STRING)
    private MusicType musicType;
    @Enumerated(EnumType.STRING)
    private SeasonType season;

    @OneToMany(mappedBy = "content", fetch = FetchType.LAZY)
    private List<MstHighlight> highlightList = new ArrayList<>();

    @Transient
    private Long parentId;
    @Transient
    private MultipartFile savedFile;
}
