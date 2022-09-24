package com.memorial.st.mst.domain.file;

import com.memorial.st.mst.domain.file.model.FileType;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ContentFilePK implements Serializable {
    private static final long serialVersionUID = -3527741493770935982L;
    private Long contentId;
    private FileType fileType;
}