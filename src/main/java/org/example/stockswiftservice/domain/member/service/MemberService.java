package org.example.stockswiftservice.domain.member.service;


import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.company.entity.Company;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.example.stockswiftservice.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;



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

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
