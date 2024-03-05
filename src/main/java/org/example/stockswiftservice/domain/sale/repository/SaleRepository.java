package org.example.stockswiftservice.domain.sale.repository;

import org.example.stockswiftservice.domain.sale.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
}
