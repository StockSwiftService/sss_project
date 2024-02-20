package org.example.stockswiftservice.domain.stock.repository;

import org.example.stockswiftservice.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByItemNameContaining(String itemName);
}
