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
@Table(name = "mst_user_consent")
public class MstUserConsent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String clientEntityId;

    private String scope;

    private LocalDateTime modDate;

    private LocalDateTime createdDate;


}
