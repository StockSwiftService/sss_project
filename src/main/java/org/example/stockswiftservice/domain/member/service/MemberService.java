package org.example.stockswiftservice.domain.member.service;


import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.example.stockswiftservice.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;



    public void repJoin(String repName, String username, String password, LocalDate birthday) {
        Member rep = Member.builder()
                .name(repName)
                .rank("대표이사")
                .username(username)
                .password(passwordEncoder.encode(password))
                .birthday(birthday)
                .authority(2)
                .build();
        this.memberRepository.save(rep);
    }
}
