package org.example.stockswiftservice.domain.company.repository;

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
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByCompanyCode(String str);
    Optional<Company> findByName(String name);
    Optional<Company> findByBusinessNumber(String businessNumber);
    Optional<Company> findByEmail(String email);
    Optional<Company> findByNameAndEmailAndBusinessNumber(String name, String email, String businessNumber);

    Page<Company> findAll(Pageable pageable);

    @Query("SELECT c FROM Company c WHERE (:isApproved = 'ALL' OR (c.isApproved = true AND :isApproved = 'APPROVED') OR (c.isApproved = false AND :isApproved = 'NOT_APPROVED')) " +
            "AND (c.businessNumber LIKE %:keyword% " +
            "OR c.email LIKE %:keyword% " +
            "OR c.name LIKE %:keyword% " +
            "OR c.repName LIKE %:keyword% " +
            "OR c.address LIKE %:keyword% " +
            "OR c.detailAddress LIKE %:keyword% " +
            "OR c.memo LIKE %:keyword%)")
    Page<Company> findByKeyword(Pageable pageable,@Param("keyword")String keyword,@Param("isApproved")String isApprove);
}
