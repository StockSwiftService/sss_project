package org.example.stockswiftservice.domain.salemanagement.repository;

import org.example.stockswiftservice.domain.salemanagement.entity.SalesManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesManagementRepository extends JpaRepository<SalesManagement, Long> {
    Optional<SalesManagement> findBySalesDate(String SalesDate);
}
