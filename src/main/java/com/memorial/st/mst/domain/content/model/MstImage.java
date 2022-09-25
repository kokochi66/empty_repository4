package com.memorial.st.mst.domain.content.model;

import com.memorial.st.mst.domain.content.MstContent;
import com.memorial.st.mst.domain.content.enumType.ImageType;
import lombok.*;

import javax.persistence.*;

@Entity
@DiscriminatorValue("IMAGE")
@Getter
@Setter
@Table(name = "mst_image")
public class MstImage extends MstContent {

    private static final long serialVersionUID = 1895665416371675768L;
    private String description;
    @Enumerated(EnumType.STRING)
    private ImageType imageType;
}
