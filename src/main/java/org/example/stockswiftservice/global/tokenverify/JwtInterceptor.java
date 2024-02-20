package org.example.stockswiftservice.global.tokenverify;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

public class JwtInterceptor implements HandlerInterceptor {
    private String secretKey;

    public JwtInterceptor(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 쿠키에서 토큰을 가져옵니다.
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            try {
                // 토큰을 검증합니다.
                Jws<Claims> claimsJws = Jwts.parser()
                        .setSigningKey(secretKey.getBytes())
                        .parseClaimsJws(token);
                // 토큰 검증이 성공한 경우, 요청을 계속 진행합니다.
                return true;
            } catch (Exception e) {
                // 토큰 검증에 실패한 경우, 401 Unauthorized 응답을 반환합니다.
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized1234");
                return false;
            }
        }

// 토큰이 없는 경우, 401 Unauthorized 응답을 반환합니다.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        return false;

    }
}
