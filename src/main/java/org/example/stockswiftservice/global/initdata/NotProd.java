package org.example.stockswiftservice.global.initdata;

import org.example.stockswiftservice.domain.company.entity.Company;
import org.example.stockswiftservice.domain.company.repository.CompanyRepository;
import org.example.stockswiftservice.domain.company.service.CompanyService;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.example.stockswiftservice.domain.member.repository.MemberRepository;
import org.example.stockswiftservice.domain.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(MemberRepository memberRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        return args -> {
//            Member member1 = memberService.join("user1", password, "user1@test.com");
//            Member member2 = memberService.join("admin@test.com", "김관리", "1234","admin");
//            Member member3 = memberService.join("user@test.com", "김헬로", "1234","user");
//
//            articleService.create(member3, "테스트입니다.", "테스트 내용입니다.");
//            articleService.create(member2, "테스트1입니다.", "테스트 내용1입니다.");
//            articleService.create(member1, "테스트2입니다.", "테스트 내용2입니다.");
//            articleService.create(member2, "테스트3입니다.", "테스트 내용3입니다.");
//            articleService.create(member1, "테스트4입니다.", "테스트 내용4입니다.");
//helloCarService.create("아반떼",  "/img/car3.png", "현대", 2000, 1500, 2024,"세단","준중형", "가솔린");
            Company adminCompany = Company.builder()
                    .name("SSS")
                    .businessNumber("0000000000")
                    .repName("최경현")
                    .email("gusrudchl12@gmail.com")
                    .companyCode("AAAAAA")
                    .address("대전광역시 서구 대덕대로 179")
                    .detailAddress("9층 sbs컴퓨터아트학원 대전점")
                    .isApproved(true)
                    .build();
            Member admin = Member.builder()
                    .name("admin")
                    .position("관리자")
                    .username("admin")
                    .password(passwordEncoder.encode("1234"))
                    .birthday(LocalDate.ofEpochDay(2024-02-19))
                    .authority(1)
                    .tokenLifeSpan(4)
                    .company(adminCompany)
                    .build();

            if (companyRepository.findByCompanyCode(adminCompany.getCompanyCode()).isEmpty()){
                companyRepository.save(adminCompany);
                memberRepository.save(admin);
            }
        };
    }
}
