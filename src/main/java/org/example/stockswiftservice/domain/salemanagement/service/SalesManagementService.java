package org.example.stockswiftservice.domain.salemanagement.service;

import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseRepository;
import org.example.stockswiftservice.domain.salemanagement.entity.SalesManagement;
import org.example.stockswiftservice.domain.salemanagement.repository.SalesManagementRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesManagementService {
    private final SalesManagementRepository saleManagementRepository;
    private final PurchaseRepository purchaseRepository;
    public SalesManagement getSalesManagement(Purchase purchase){
        Optional<SalesManagement> salesManagement = saleManagementRepository.findBySalesDate(String.valueOf(purchase.getPurchaseDate()));

        return salesManagement.get();
    }

    public SalesManagement printTotalSales(Purchase purchase){
        SalesManagement salesManagement = getSalesManagement(purchase);
        LocalDate date = purchase.getPurchaseDate();
        // 일간 매출
        Long dailySales = purchaseRepository.getSalesByDate(date);
        int dailyNum = salesManagement.getDailySalesNumber();

        // 주간 매출
        Long weeklySales = printSalesOfWeek(purchase);
        int weekNum = salesManagement.getWeekSalesNumber();

        // 월간 매출
        Long monthlySales = printMonthlySales(purchase);
        int monthNum = salesManagement.getMonthSalesNumber();

        // 저장
        salesManagement.setSalesDate(String.valueOf(date));
        salesManagement.setDailyTotalSales(dailySales);
        salesManagement.setWeekTotalSales(weeklySales);
        salesManagement.setMonthTotalSales(monthlySales);
        salesManagement.setDailySalesNumber(dailyNum + 1);
        salesManagement.setWeekSalesNumber(weekNum + 1);
        salesManagement.setMonthSalesNumber(monthNum + 1);

        saleManagementRepository.save(salesManagement);

        return salesManagement;
    }
    public Long printSalesOfWeek(Purchase purchase){
        LocalDate date = purchase.getPurchaseDate();
        int year = date.getYear();
        int week = (int) WeekFields.of(DayOfWeek.MONDAY, 1).weekOfYear().getFrom(date);
        Long weeklySales = purchaseRepository.getSalesByWeek(year, week);
        return weeklySales;
    }

    public Long printMonthlySales(Purchase purchase){
        LocalDate date = purchase.getPurchaseDate();
        int year = date.getYear();
        int month = date.getDayOfMonth();
        Long monthlySales = purchaseRepository.getSalesByMonth(year, month);
        return monthlySales;
    }
}
