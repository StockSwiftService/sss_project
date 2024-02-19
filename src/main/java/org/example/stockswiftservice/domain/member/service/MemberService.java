package org.example.stockswiftservice.domain.member.service;


import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.company.entity.Company;
import org.example.stockswiftservice.domain.company.repository.CompanyRepository;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.example.stockswiftservice.domain.member.repository.MemberRepository;
import org.example.stockswiftservice.global.jwt.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CompanyRepository companyRepository;


    public void repJoin(String repName, String username, String password, LocalDate birthday, Company companyCode) {
        Member rep = Member.builder()
                .name(repName)
                .position("대표이사")
                .username(username)
                .password(passwordEncoder.encode(password))
                .birthday(birthday)
                .company(companyCode)
                .authority(2)
                .build();
        this.memberRepository.save(rep);
    }

    public String genAccessToken(String username, String companyCode) {
        Member member = findByUsernameAndCompanyCode(username, companyCode).orElse(null);

        if (member == null) return null;


        return jwtProvider.genToken(member.toClaims(), 60 * 60 * member.getTokenLifeSpan());
    }

    public Optional<Member> findByUsernameAndCompanyCode(String username, String companyCode) {
        Company company = this.companyRepository.findByCompanyCode(companyCode).orElse(null);
        return this.memberRepository.findByUsernameAndCompany(username, company);
    }

    public String findRepUsernameByCompany(String companyCode) {
        Company company = this.companyRepository.findByCompanyCode(companyCode).orElse(null); //코드로 회사 찾기
        Optional<Member> member = this.memberRepository.findByCompany(company);
        if (member.isPresent() && member.get().getAuthority() == 2) {
            return member.get().getUsername(); //아이디 얻기
        }
        return null;
    }

    public Optional<Member> findByUsername(String username) {
        return this.memberRepository.findByUsername(username);
    }

    public String genRefreshToken(String username, String companyCode) {
        Member member = findByUsernameAndCompanyCode(username, companyCode).orElse(null);

        if (member == null) return null;

        return jwtProvider.genToken(member.toClaims(), 60 * 60 * 24 * 365);
    }

    public String genNewAccessToken(String username) {
        Member member = findByUsername(username).orElse(null);

        if (member == null) return null;

        return jwtProvider.genToken(member.toClaims(), 60 * 60 * member.getTokenLifeSpan());
    }

    public boolean userCheck(String companyCode, String username, String password) {
        Optional<Member> optionalMember = findByUsernameAndCompanyCode(username, companyCode);
        Member searchMember = optionalMember.orElse(null);

        if (searchMember != null) {
            return passwordEncoder.matches(password, searchMember.getPassword());
        } else {
            return false;
        }
    }

    //비번 수정
    public Member PwModify(String newPassword, String username, String companyCode) {
        Member member = findByUsernameAndCompanyCode(username, companyCode).orElse(null);
//        Member member = memberRepository.findByUsername(username) //유저 찾기
//                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
//        Company company = companyRepository.findByCompanyCode(companyCode).orElse(null);
//
        Member modifiedMember = Member.builder()
                .id(member.getId())
                .name(member.getName())
                .position(member.getPosition())
                .username(member.getUsername())
                .password(passwordEncoder.encode(newPassword))
                .birthday(member.getBirthday())
                .authority(member.getAuthority())
                .tokenLifeSpan(member.getTokenLifeSpan())
                .company(member.getCompany())
                .build();

        memberRepository.save(modifiedMember);
        return modifiedMember;
    }
}