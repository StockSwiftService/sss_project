package org.example.stockswiftservice.domain.stock.repository;

import org.example.stockswiftservice.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query(value = "SELECT * FROM stock WHERE item_name LIKE %?1% OR client_name LIKE %?1%", nativeQuery = true)
    List<Stock> findByItemNameOrClientNameContaining(String searchText);
}
