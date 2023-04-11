package com.memorial.st.mst.controller.user;

import com.google.common.util.concurrent.RateLimiter;
import com.memorial.st.mst.controller.user.model.MstUserResponse;
import com.memorial.st.mst.domain.user.MstUser;
import com.memorial.st.mst.interceptor.AuthExcludes;
import com.memorial.st.mst.service.user.UserService;
import com.memorial.st.mst.service.user.repository.MstUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@RequestMapping(value = "/user")
public class UserController {
    // 사용자 관련 API

    @Autowired
    private UserService userService;

    // 로그인
    @AuthExcludes
    @PostMapping("/login")
    public Boolean login(@RequestBody MstUser user, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            log.info("/user/login - 로그인 {}", user);
            String jwtToken = userService.userLogin(user.getUserId(), user.getPassword());
            response.setHeader("Authorization", "Bearer " + jwtToken); // 표준화된 형식에 맞게 JWT 토큰을 Authorization 헤더에 담기
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 회원가입
    @AuthExcludes
    @PostMapping("/register")
    public MstUserResponse register(@RequestBody MstUser user, HttpServletResponse response) throws Exception {
        log.info("/user/register - 회원가입");
        return new MstUserResponse(userService.register(user));
    }
}
