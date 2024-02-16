package org.example.stockswiftservice.domain.salemanagement.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.global.baseentity.BaseEntity;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SalesManagement extends BaseEntity {
    // 해당 날짜
    private String salesDate;

    // 일간 매출
    private Long dailyTotalSales;

    // 주간 매출
    private Long weekTotalSales;

    // 월 매출
    private Long monthTotalSales;

    // 일간 건수
    private int dailySalesNumber;

    // 주간 건수
    private int weekSalesNumber;

    // 월간 건수
    private int monthSalesNumber;

    @OneToMany(mappedBy = "salesManagement" , cascade = CascadeType.REMOVE)
    private List<Purchase> purchase;
}
