package com.memorial.st.mst.domain.content.model;

import com.memorial.st.mst.domain.content.MstContent;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

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
}
