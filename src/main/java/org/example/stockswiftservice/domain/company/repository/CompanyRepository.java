package org.example.stockswiftservice.domain.company.repository;

import org.example.stockswiftservice.domain.company.entity.Company;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByCompanyCode(String str);
    Optional<Company> findByName(String name);
    Optional<Company> findByBusinessNumber(String businessNumber);
    Optional<Company> findByEmail(String email);
    Optional<Company> findByNameAndEmailAndBusinessNumber(String name, String email, String businessNumber);
}
