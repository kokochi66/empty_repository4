package com.memorial.st.mst.domain.content.model;

import com.memorial.st.mst.domain.content.MstContent;
import com.memorial.st.mst.domain.content.enumType.SeasonType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("ANIME")
@Getter
@Setter
@Table(name = "mst_anime")
public class MstAnime extends MstContent {

    private String title;
    private String director;

    @Enumerated(EnumType.STRING)
    private SeasonType season;

    @Transient
    private List<MultipartFile> keyVisuals;
}
