package org.example.stockswiftservice.domain.salemanagement.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.stockswiftservice.global.baseentity.BaseEntity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SalesManagement extends BaseEntity {
    // 해당 날짜
    private String salesDate;

    // 해당 날짜의 연도
    private int year;

    // 해당 날짜의 월
    private int month;

    // 해당 주차
    private int weekDate;

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

}
