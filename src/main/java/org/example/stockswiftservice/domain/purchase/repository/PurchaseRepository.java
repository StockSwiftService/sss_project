package org.example.stockswiftservice.domain.purchase.repository;

import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Query("SELECT SUM(p.purchaseTotal) FROM Purchase p WHERE p.purchaseDate = :date")
    Long getSalesByDate(@Param("date") LocalDate date);

    @Query(value = "SELECT SUM(purchase_total) FROM Purchase WHERE YEAR(purchase_date) = :year AND WEEK(purchase_date, 3) = :week", nativeQuery = true)
    Long getSalesByWeek(@Param("year") int year, @Param("week") int week);


    @Query("SELECT SUM(p.purchaseTotal) FROM Purchase p WHERE YEAR(p.purchaseDate) = :year AND MONTH(p.purchaseDate) = :month")
    Long getSalesByMonth(@Param("year") int year, @Param("month") int month);
}
