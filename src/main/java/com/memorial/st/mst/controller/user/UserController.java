package com.memorial.st.mst.controller.user;

import com.memorial.st.mst.domain.user.MstUser;
import com.memorial.st.mst.interceptor.AuthExcludes;
import com.memorial.st.mst.service.user.UserService;
import com.memorial.st.mst.service.user.repository.MstUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequestMapping(value = "/user")
public class UserController {
    // 사용자 관련 API

    @Autowired
    private UserService userService;


    // 로그인
    @AuthExcludes
    @PostMapping("/auth/login")
    public String login(@RequestBody MstUser user, HttpServletResponse response) throws Exception {
        log.info("/user/login - 로그인 {}", user);
        Cookie cookie = new Cookie("PRJ-MST-CENT-USER", userService.userLogin(user.getUserId()));
        response.addCookie(cookie);
        return "로그인 성공";
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("/user/logout - 로그아웃");
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("PRJ-MST-CENT-USER")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        return "로그아웃 성공";
    }

    // 회원가입
    @AuthExcludes
    @PostMapping("/register")
    public String register(@RequestBody MstUser user, HttpServletResponse response) throws Exception {
        log.info("/user/register - 회원가입");
        userService.register(user);
        return "회원가입 성공";
    }

    // 사용자 인증
    @GetMapping("/info")
    public MstUser userInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("/user/info - 사용자 정보 가져오기");

        String decryptValue = null;

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("PRJ-MST-CENT-USER")) {
                    decryptValue = cookie.getValue();
                    break;
                }
            }
        }

        log.info("TEST :: cookieString = {}", decryptValue);
        MstUser user = userService.getUserCookie(decryptValue);
        return user;
    }

    // 사용자 데이터 가져오기
}
