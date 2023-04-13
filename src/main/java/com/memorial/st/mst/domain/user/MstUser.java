package com.memorial.st.mst.domain.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    // 사용자 개인정보
    private String gender;  // 성별
    private LocalDateTime birthDate; // 생년월일
    private String email;
    private String address;         // 주소
    private String nationality;     //  국적
    private String profileImgUrl;      // 프로필 이미지url
}
