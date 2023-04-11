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
public class MstUserResponse {
    private Long userId;
    private String userName;
    private String nickName;
    private MstRole role;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public MstUserResponse(MstUser user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.nickName = user.getNickName();
        this.role = user.getRole();
        this.regDate = user.getRegDate();
        this.modDate = user.getModDate();
    }
}
