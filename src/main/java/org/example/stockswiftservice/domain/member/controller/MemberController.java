package org.example.stockswiftservice.domain.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.example.stockswiftservice.domain.member.service.MemberService;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/member", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class MemberController {
    private final MemberService memberService;


    @Data
    public static class LoginRequest {
        @NotNull
        private String username;
        @NotNull
        private String password;
        @NotNull
        private String companyCode;
    }
    @Getter
    public static class loginresponse {

        private String accessToken;
        private String refreshToken;

        public loginresponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
    @PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE)
    public RsData<loginresponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse resp) {

        boolean userCheck = this.memberService.userCheck(loginRequest.companyCode,loginRequest.username,loginRequest.password);

        if(userCheck) {
            String accessToken = memberService.genAccessToken(loginRequest.getUsername(), loginRequest.getCompanyCode());
            String refreshToken = memberService.genRefreshToken(loginRequest.getUsername(), loginRequest.getCompanyCode());

            if (accessToken == null) {
                return RsData.of("Invalid username or password", null);
            }

            // 쿠키에 액세스 토큰 저장
            Cookie accessCookie = new Cookie("access_token", accessToken);
            accessCookie.setHttpOnly(false);
            resp.addCookie(accessCookie);

            // 쿠키에 리프레시 토큰 저장
            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
            refreshCookie.setHttpOnly(true);
            resp.addCookie(refreshCookie);

            return RsData.of("S-1", "토큰이 생성되었습니다.", null);
        }else {
            return RsData.of("Invalid username or password", null);
        }

    }
}
