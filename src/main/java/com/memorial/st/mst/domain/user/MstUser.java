package com.memorial.st.mst.domain.user;

import com.memorial.st.mst.domain.file.ContentFile;
import com.memorial.st.mst.domain.music.model.MusicType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mst_user")
public class MstUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String userName;
    private String nickName;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MstRole role;

    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
