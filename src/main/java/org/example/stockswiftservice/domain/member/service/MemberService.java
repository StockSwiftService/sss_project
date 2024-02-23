package org.example.stockswiftservice.domain.member.service;


import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.company.entity.Company;
import org.example.stockswiftservice.domain.company.repository.CompanyRepository;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.example.stockswiftservice.domain.member.repository.MemberRepository;
import org.example.stockswiftservice.global.jwt.JwtProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
                .tokenLifeSpan(4)
                .build();
        this.memberRepository.save(rep);
    }

    public Member employeeJoin(String name, String position, int authority, String username, String password, LocalDate birthday, Long userId) {

        Member company = this.memberRepository.findById(userId).orElse(null);
        Member employee = Member.builder()
                .name(name)
                .position(position)
                .authority(authority)
                .username(username)
                .password(passwordEncoder.encode(password))
                .birthday(birthday)
                .tokenLifeSpan(4)
                .company(company.getCompany())
                .build();
        this.memberRepository.save(employee);
        return employee;
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
        Optional<Member> member = this.memberRepository.findByCompanyAndAuthority(company, 2);

        //아이디 얻기
        return member.map(Member::getUsername).orElse(null);
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
            if (searchMember.getCompany().isApproved()) {
                return passwordEncoder.matches(password, searchMember.getPassword());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean adminCheck(String companyCode, String username) {
        Optional<Member> optionalMember = findByUsernameAndCompanyCode(username, companyCode);
        Member searchMember = optionalMember.orElse(null);

        return searchMember != null && searchMember.getAuthority() == 1;
    }

    //비번 수정
    public Member PwModify(String newPassword, String username, String companyCode) {
        Member member = findByUsernameAndCompanyCode(username, companyCode).orElse(null);
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

    public Optional<Member> findbyId(Long id) {
        return this.memberRepository.findById(id);
    }

    public List<Member> getEmployeeList(String companyCode) {
        Company company = this.companyRepository.findByCompanyCode(companyCode).orElse(null);
        return this.memberRepository.findByCompany(company);
    }

    public void deleteMember(Long memberId) {
        Member member = this.memberRepository.findById(memberId).orElse(null);
        this.memberRepository.delete(member);
    }

    public Member employeeModify(Long id, String employeeName, String position, int authority, String username, LocalDate birthday) {
        Member member = this.memberRepository.findById(id).orElse(null);
        Member modifiedMember = Member.builder()
                .id(id)
                .name(employeeName)
                .position(position)
                .authority(authority)
                .username(username)
                .password(passwordEncoder.encode(member.getPassword()))
                .birthday(birthday)
                .tokenLifeSpan(4)
                .company(member.getCompany())
                .build();

        memberRepository.save(modifiedMember);
        return modifiedMember;
    }

    public Member modifyPassword(Long id, String employeeName, String position, int authority, String username, String newPassword, LocalDate birthday) {
        Member member = this.memberRepository.findById(id).orElse(null);
        Member modifiedMember = Member.builder()
                .id(id)
                .name(employeeName)
                .position(position)
                .authority(authority)
                .username(username)
                .password(passwordEncoder.encode(newPassword))
                .birthday(birthday)
                .tokenLifeSpan(4)
                .company(member.getCompany())
                .build();

        memberRepository.save(modifiedMember);
        return modifiedMember;
    }

    public Page<Member> pagingFindAll(int page, String keyWord) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.memberRepository.findByKeyword(pageable,keyWord);
    }
}
