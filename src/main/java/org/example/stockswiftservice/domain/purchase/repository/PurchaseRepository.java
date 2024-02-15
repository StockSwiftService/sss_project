package org.example.stockswiftservice.domain.purchase.repository;

import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
