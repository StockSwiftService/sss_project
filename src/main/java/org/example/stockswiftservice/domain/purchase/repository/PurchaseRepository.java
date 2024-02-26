package org.example.stockswiftservice.domain.purchase.repository;

import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Query("SELECT SUM(p.purchaseTotal) FROM Purchase p WHERE p.purchaseDate = :date")
    Long getSalesByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(p) FROM Purchase p WHERE p.purchaseDate = :date")
    int getCountByDate(@Param("date") LocalDate date);

    @Query(value = "SELECT SUM(purchase_total) FROM Purchase WHERE YEARWEEK(purchase_date, 3) = YEARWEEK(:date, 3)", nativeQuery = true)
    Long getSalesByWeek(@Param("date") LocalDate date);

    @Query(value = "SELECT COUNT(*) FROM Purchase WHERE YEARWEEK(purchase_date, 3) = YEARWEEK(:date, 3)", nativeQuery = true)
    int getCountByWeek(@Param("date") LocalDate date);

    @Query("SELECT SUM(p.purchaseTotal) FROM Purchase p WHERE YEAR(p.purchaseDate) = :year AND MONTH(p.purchaseDate) = :month")
    Long getSalesByMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COUNT(p) FROM Purchase p WHERE YEAR(p.purchaseDate) = :year AND MONTH(p.purchaseDate) = :month")
    int getCountByMonth(@Param("year") int year, @Param("month") int month);

    List<Purchase> findByPurchaseDate(LocalDate date);
}
