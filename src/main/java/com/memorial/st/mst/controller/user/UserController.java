package com.memorial.st.mst.controller.user;

import com.memorial.st.mst.controller.user.model.MstUserRequest;
import com.memorial.st.mst.controller.user.model.MstUserResponse;
import com.memorial.st.mst.controller.user.model.UserConsentRequest;
import com.memorial.st.mst.domain.user.service.UserConsentService;
import com.memorial.st.mst.domain.user.service.UserService;
import com.memorial.st.mst.interceptor.AuthExcludes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequestMapping(value = "/user")
public class UserController {
    // 사용자 관련 API

    @Autowired
    private UserService userService;
    @Autowired
    private UserConsentService userConsentService;

    // 로그인
    @AuthExcludes
    @PostMapping("/login")
    public Boolean login(@RequestBody MstUserRequest request, HttpServletResponse response) throws Exception {
        try {
            String jwtToken = userService.userLogin(request.getUserId(), request.getPassword());
            response.setHeader("Authorization", "Bearer " + jwtToken); // 표준화된 형식에 맞게 JWT 토큰을 Authorization 헤더에 담기
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 회원가입
    @AuthExcludes
    @PostMapping("/register")
    public MstUserResponse register(@RequestBody MstUserRequest request, HttpServletResponse response) throws Exception {
        return new MstUserResponse(userService.register(request.getUserName(), request.getNickName(), request.getPassword(), request.getRole()));
    }

    // 사용자 정보 동의
    @PostMapping("/consent")
    public Boolean createConsent(@RequestBody UserConsentRequest consentRequest) {
        userConsentService.createConsent(consentRequest);
        return true;
    }
}
