package com.memorial.st.mst.domain.content;

import com.memorial.st.mst.domain.file.ContentFilePK;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
public abstract class MstContent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long id;
    private String fileSrc;
    private Integer point;

    @CreatedDate
    private LocalDateTime regDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private MstContent parent;

    @OneToMany(mappedBy = "parent")
    private List<MstContent> child = new ArrayList<>();

    @OneToMany(mappedBy = "content")
    private List<MstHighlight> highlightList = new ArrayList<>();

    @Transient
    private MultipartFile savedFile;
}
