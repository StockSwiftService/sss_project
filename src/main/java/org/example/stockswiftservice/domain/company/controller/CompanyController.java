package org.example.stockswiftservice.domain.company.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.company.entity.Company;
import org.example.stockswiftservice.domain.company.service.CompanyService;
import org.example.stockswiftservice.domain.member.service.MemberService;
import org.hibernate.annotations.Comment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/company", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class CompanyController {

    private final CompanyService companyService;
    private final MemberService memberService;



    @Getter
    public static class JoinRequest {

        //기업명
        private String name;

        //사업자번호
        private String businessNumber;

        // 대표 이름
        private String repName;

        //대표 이메일
        private String email;

        //대표 아이디
        private String username;

        //대표 비밀번호
        private String password;

        //대표 생일
        private LocalDate birthday;

    }
    @PostMapping(value = "/join", consumes = APPLICATION_JSON_VALUE)
    public void join(@RequestBody JoinRequest joinRequest, HttpServletResponse resp){

        String companyCode = this.companyService.join(joinRequest.name,joinRequest.businessNumber,joinRequest.repName,joinRequest.email);

        this.memberService.repJoin(joinRequest.repName,joinRequest.username,joinRequest.password,joinRequest.birthday);

    }
}


