package org.example.stockswiftservice.domain.company.repository;

import org.example.stockswiftservice.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {
    Optional<Company> findByCompanyCode(String str);
}
