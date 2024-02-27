package org.example.stockswiftservice.domain.stock.repository;

import org.example.stockswiftservice.domain.stock.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("SELECT s FROM Stock s WHERE s.client.clientName = :clientName AND s.itemName = :itemName")
    Optional<Stock> findByItemName(@Param("clientName") String clientName, @Param("itemName") String itemName);

    Page<Stock> findAll(Pageable pageable);
    Page<Stock> findAll(Specification<Stock> spec, Pageable pageable);
    @Query(value = "SELECT * FROM stock WHERE item_name LIKE %?1% OR client_name LIKE %?1%", nativeQuery = true)
    List<Stock> findByItemNameOrClientNameContaining(String searchText);
}
