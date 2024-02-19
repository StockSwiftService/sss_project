package org.example.stockswiftservice.domain.member.repository;


import org.example.stockswiftservice.domain.company.entity.Company;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByUsernameAndCompany(String username, Company company);

    Optional<Member> findByCompany(Company company);
}