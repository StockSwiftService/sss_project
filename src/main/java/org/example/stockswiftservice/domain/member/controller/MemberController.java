package org.example.stockswiftservice.domain.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.http.ResponseCookie;
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


    @Getter
    public static class loginresponse {

        private String accessToken;
        private String refreshToken;

        public loginresponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    @Data
    public static class LoginRequest {
        @NotNull
        private String username;
        @NotNull
        private String password;
        @NotNull
        private String companyCode;
    }

    @PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE)
    public RsData<loginresponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse resp) {

        boolean userCheck = this.memberService.userCheck(loginRequest.companyCode, loginRequest.username, loginRequest.password);

        if (userCheck) {
            String accessToken = memberService.genAccessToken(loginRequest.getUsername(), loginRequest.getCompanyCode());
            String refreshToken = memberService.genRefreshToken(loginRequest.getUsername(), loginRequest.getCompanyCode());

            ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                    .path("/")
                    .sameSite("None")
                    .secure(true)
                    .httpOnly(true)
                    .build();
            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                    .path("/")
                    .sameSite("None")
                    .secure(true)
                    .httpOnly(true)
                    .build();

            resp.addHeader("Set-Cookie", accessCookie.toString());
            resp.addHeader("Set-Cookie", refreshCookie.toString());

            if (this.memberService.adminCheck(loginRequest.getCompanyCode(), loginRequest.getUsername())) {
                return RsData.of("S-0", "관리자 토큰이 생성되었습니다", null);
            } else {
                return RsData.of("S-1", "토큰이 생성되었습니다.", null);
            }
        } else {
            return RsData.of("Invalid username or password", null);
        }

    }

    public void TokenExtension(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    String accessToken = cookie.getValue();
                    // 토큰을 사용하는 로직
                }
                if ("refresh_token".equals(cookie.getName())) {
                    String refreshToken = cookie.getValue();
                    // 토큰을 사용하는 로직
                }
            }
        }
    }
}
