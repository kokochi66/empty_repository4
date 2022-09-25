package com.memorial.st.mst.domain.content.model;

import com.memorial.st.mst.domain.content.MstContent;
import com.memorial.st.mst.domain.content.MstHighlight;
import com.memorial.st.mst.domain.content.MstSeason;
import com.memorial.st.mst.domain.content.enumType.SeasonType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("ANIME")
@Getter
@Setter
@Table(name = "mst_anime")
public class MstAnime extends MstContent {

    private static final long serialVersionUID = -4275677627427954658L;
    private String title;
    private String director;

    @OneToMany(mappedBy = "content")
    private List<MstSeason> seasonList = new ArrayList<>();

    @Transient
    private List<MultipartFile> keyVisuals;
}
