package org.example.stockswiftservice.domain.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.example.stockswiftservice.domain.member.service.MemberService;
import org.example.stockswiftservice.global.jwt.JwtProvider;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.stockswiftservice.domain.global.filter.JwtAuthorizationFilter.extractAccessToken;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/member", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class MemberController {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @Data
    public static class EmployeeJoinRequest {

        //사원 이름
        @NotNull
        private String employeeName;
        //직급
        @NotNull
        private String position;
        //권한
        private int authority;
        //아이디
        @NotNull
        private String username;
        //사원 비밀번호
        @NotNull
        private String password;
        //사원 생일
        @NotNull
        private LocalDate birthday;
    }

    @AllArgsConstructor
    @Getter
    public static class EmployeeJoinReponse {
        private final Member member;
    }

    //사원 등록
    @PostMapping(value = "/join", consumes = APPLICATION_JSON_VALUE)
    public RsData<EmployeeJoinReponse> employeeJoin(@Valid @RequestBody EmployeeJoinRequest employeeJoinRequest, HttpServletRequest request) {
        String token = extractAccessToken(request); //헤더에 담긴 쿠키에서 토큰 요청
        Long userId = ((Integer) jwtProvider.getClaims(token).get("id")).longValue(); //유저의 아이디 값

        Member member = this.memberService.employeeJoin(employeeJoinRequest.employeeName, employeeJoinRequest.position, employeeJoinRequest.authority, employeeJoinRequest.username, employeeJoinRequest.password, employeeJoinRequest.birthday, userId);
        return RsData.of("S-1", "가입 성공", new EmployeeJoinReponse(member));
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
    @PostMapping(value = "/logout", consumes = APPLICATION_JSON_VALUE)
    public RsData<loginresponse> logout(HttpServletResponse resp) {

        Cookie expireAccessCookie = new Cookie("access_token", "");
        expireAccessCookie.setMaxAge(0);
        expireAccessCookie.setPath("/");
        resp.addCookie(expireAccessCookie);

        Cookie expireRefreshCookie = new Cookie("refresh_token", "");
        expireRefreshCookie.setMaxAge(0);
        expireRefreshCookie.setPath("/");
        resp.addCookie(expireRefreshCookie);
        return RsData.of("S-2", "토큰이 삭제되었습니다.", null);
    }


    @AllArgsConstructor
    @Getter
    public static class loginUser {
        private final Member member;
    }
    @GetMapping(value = "/loginUser", consumes = APPLICATION_JSON_VALUE)
    public RsData<?> loginUser (HttpServletRequest request){
        String token = extractAccessToken(request); //헤더에 담긴 쿠키에서 토큰 요청
        Long userId = ((Integer) jwtProvider.getClaims(token).get("id")).longValue(); //유저의 아이디 값

        Member loginUser = this.memberService.findbyId(userId).orElse(null);
        return RsData.of("S-99","현재 로그인 유저",new loginUser(loginUser));
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

    @AllArgsConstructor
    @Getter
    public static class MembersResponse {
        private final List<Member> memberList;
        private final Page<Member> pagingList;
    }

    //회원 리스트
    @GetMapping(value = "/user-manages", consumes = APPLICATION_JSON_VALUE)
    public RsData<MembersResponse> employeeList(HttpServletRequest request,
                                                @RequestParam(value="page", defaultValue="0") int page,
                                                @RequestParam(value="keyWord", defaultValue="")String keyWord) {
        String token = extractAccessToken(request); //헤더에 담긴 쿠키에서 토큰 요청
        Long userId = ((Integer) jwtProvider.getClaims(token).get("id")).longValue(); //유저의 회사코드 값
        Member member = memberService.findbyId(userId).orElse(null);

        List<Member> memberList = this.memberService.getEmployeeList(member.getCompany().getCompanyCode());
        Page<Member> pagingList = this.memberService.pagingFindAll(page,keyWord);

        if (member.getAuthority() == 1 || member.getAuthority() == 2) {
            List<Member> filteredList = memberList.stream()
                    .filter(m -> m.getAuthority() != 1 && m.getAuthority() != 2)
                    .collect(Collectors.toList());
            return RsData.of("S-2", "성공", new MembersResponse(filteredList,pagingList));
        } else {
            // 권한이 1 또는 2가 아닌 경우 전체 목록 리턴
            return RsData.of("S-2", "성공", new MembersResponse(memberList,pagingList));
        }
    }

    @AllArgsConstructor
    @Getter
    public static class DeleteResponse {
        private final List<Member> memberList;
    }

    //회원 삭제
    @DeleteMapping(value = "/delete", consumes = APPLICATION_JSON_VALUE)
    public RsData<DeleteResponse> delete(@RequestParam("ids") Long[] memberId, HttpServletRequest request) {
        for (Long id : memberId) {
            this.memberService.deleteMember(id);
        }
        return RsData.of("S-3", "삭제 성공", null);
    }


    @AllArgsConstructor
    @Getter
    public static class ModifyReponse {
        private final Member member;
    }

    @Data
    public static class ModifyEmployeeRequest {

        private Long id;
        //사원 이름
        private String employeeName;
        //직급
        private String position;
        //권한
        private int authority;
        //아이디
        private String username;
        //비번
        private String password;
        //사원 생일
        private LocalDate birthday;
    }

    //회원 수정
    @PostMapping(value = "/modify", consumes = APPLICATION_JSON_VALUE)
    public RsData<ModifyReponse> modify(@RequestBody ModifyEmployeeRequest modifyEmployeeRequest) {
        Member member = this.memberService.employeeModify(modifyEmployeeRequest.getId(), modifyEmployeeRequest.getEmployeeName(), modifyEmployeeRequest.getPosition(), modifyEmployeeRequest.getAuthority(), modifyEmployeeRequest.getUsername(), modifyEmployeeRequest.getBirthday());
        return RsData.of("S-4", "사원 수정 성공", new ModifyReponse(member));
    }

    //비번 초기화
    @PostMapping(value = "/modify-password", consumes = APPLICATION_JSON_VALUE)
    public RsData<ModifyReponse> modifyPassword(@RequestBody ModifyEmployeeRequest modifyEmployeeRequest) {
        Member member = this.memberService.modifyPassword(modifyEmployeeRequest.getId(), modifyEmployeeRequest.getEmployeeName(), modifyEmployeeRequest.getPosition(), modifyEmployeeRequest.getAuthority(), modifyEmployeeRequest.getUsername(), modifyEmployeeRequest.getPassword(), modifyEmployeeRequest.getBirthday());
        return RsData.of("S-4", "비번 수정 성공", new ModifyReponse(member));
    }
}
