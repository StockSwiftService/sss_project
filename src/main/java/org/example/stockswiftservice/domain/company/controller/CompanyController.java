package org.example.stockswiftservice.domain.company.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.company.entity.Company;
import org.example.stockswiftservice.domain.company.service.CompanyService;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.example.stockswiftservice.domain.member.service.MemberService;
import org.example.stockswiftservice.global.rs.RsData;
import org.hibernate.annotations.Comment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.MimeTypeUtils.ALL_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/company", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class CompanyController {

    private final CompanyService companyService;
    private final MemberService memberService;


    @Data
    public static class JoinRequest {

        //기업명
        @NotNull
        private String name;

        //사업자번호
        @NotNull
        private String businessNumber;

        // 대표 이름
        @NotNull
        private String repName;

        @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$")
        @NotNull
        //대표 이메일
        private String email;

        //대표 아이디
        @NotNull
        private String username;

        //        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")
        //대표 비밀번호
        @NotNull
        private String password;

        //대표 생일
        @NotNull
        private LocalDate birthday;

        //주소
        @NotNull
        private String address;

        //상세 주소
        @NotNull
        private String detailAddress;
    }

    @AllArgsConstructor
    @Getter
    public static class CompanyReponse {
        private final Company company;
    }

    @PostMapping(value = "/join", consumes = APPLICATION_JSON_VALUE)
    public RsData<CompanyReponse> join(@Valid @RequestBody JoinRequest joinRequest, HttpServletResponse resp) {

        Company companyCode = this.companyService.join(joinRequest.name, joinRequest.businessNumber, joinRequest.repName, joinRequest.email, joinRequest.address, joinRequest.detailAddress);
        this.memberService.repJoin(joinRequest.repName, joinRequest.username, joinRequest.password, joinRequest.birthday, companyCode);

        return RsData.of("S-1", "가입 성공", null);
    }

    //회사명 검증
    @AllArgsConstructor
    @Getter
    public static class NameResponse {
        private final Optional<Company> company;
    }

    @Data
    public static class NameRequest {
        @NotNull
        private String name;
    }

    @PostMapping(value = "/check-name", consumes = ALL_VALUE)
    public RsData<NameResponse> checkNameDuplicate(@Valid @RequestBody NameRequest nameRequest) {
        Optional<Company> name = companyService.findByName(nameRequest.getName());
        if (name.isPresent()) {
            return RsData.of("S-2", "중복된 아이디", new NameResponse(name));
        } else {
            return RsData.of("S-3", "아이디 사용 가능", null);
        }
    }

    //사업자 검증
    @AllArgsConstructor
    @Getter
    public static class BusinessNumberResponse {
        private final Optional<Company> company;
    }

    @Data
    public static class BusinessNumberRequest {
        @NotNull
        @Size(min = 10, max = 10)
        @Pattern(regexp = "\\d{10}")
        private String businessNumber;
    }

    @PostMapping(value = "/check-businessNumber", consumes = ALL_VALUE)
    public RsData<BusinessNumberResponse> checkNameDuplicate(@Valid @RequestBody BusinessNumberRequest businessNumberRequest) {
        Optional<Company> businessNumber = companyService.findByBusinessNumber(businessNumberRequest.getBusinessNumber());
        if (businessNumber.isPresent()) {
            return RsData.of("S-2", "존재하는 사업자 번호", new BusinessNumberResponse(businessNumber));
        } else {
            return RsData.of("S-3", "사업자 번호 등록 가능", null);
        }
    }

    //대표 아이디 검증
    @AllArgsConstructor
    @Getter
    public static class UsernameResponse {
        private final Optional<Member> member;
    }

    @Data
    public static class UsernameRequest {
        @NotNull
        private String username;
    }

    @PostMapping(value = "/check-username", consumes = ALL_VALUE)
    public RsData<UsernameResponse> checkUsernameDuplicate(@Valid @RequestBody UsernameRequest usernameRequest) {
        Optional<Member> username = memberService.findByUsername(usernameRequest.getUsername());
        if (username.isPresent()) {
            return RsData.of("S-7", "아이디 중복", new UsernameResponse(username));
        } else {
            return RsData.of("S-8", "아이디 사용가능", null);
        }
    }


    //아이디 찾기
    @Data
    public static class SearchCodeValue {
        @NotNull
        private String name;
        @NotNull
        @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$")
        private String email;
        @NotNull
        private String businessNumber;
    }

    @AllArgsConstructor
    @Getter
    public static class CodeSearchResponse {
        private final String company;
    }

    @PostMapping(value = "/code-search", consumes = ALL_VALUE)
    public RsData<CodeSearchResponse> codeSearch(@Valid @RequestBody SearchCodeValue searchCodeValue) {
        Optional<Company> company = companyService.findByNameAndEmailAndBusinessNumber(searchCodeValue.getName(), searchCodeValue.getEmail(), searchCodeValue.getBusinessNumber());
        if (company != null) {
            return RsData.of("S-9", "해당 회원의 아이디", new CodeSearchResponse("회원님의 회사 코드는 \"" + company.get().getCompanyCode() + "\" 입니다."));
        } else {
            return RsData.of("S-10", "해당 회원 없음", null);
        }
    }
}


