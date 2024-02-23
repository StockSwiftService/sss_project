package org.example.stockswiftservice.domain.member.repository;


import org.example.stockswiftservice.domain.company.entity.Company;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByUsernameAndCompany(String username, Company company);

    Optional<Member> findByCompanyAndAuthority(Company company, int authority);

    List<Member> findByCompany(Company company);

    Page<Member> findAll(Pageable pageable);

    @Query("SELECT m FROM Member m WHERE "+
            "m.name LIKE %:keyword% OR " +
            "m.position LIKE %:keyword% OR " +
            "m.username LIKE %:keyword%")
    Page<Member> findByKeyword(Pageable pageable,@Param("keyword")String keyword);
}
