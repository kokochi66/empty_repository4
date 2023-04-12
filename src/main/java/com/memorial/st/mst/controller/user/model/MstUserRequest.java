package com.memorial.st.mst.controller.user.model;

import com.memorial.st.mst.domain.user.MstRole;
import com.memorial.st.mst.domain.user.MstUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MstUserRequest {
    private Long userId;
    private String userName;
    private String nickName;
    private String password;
    private MstRole role;
}
