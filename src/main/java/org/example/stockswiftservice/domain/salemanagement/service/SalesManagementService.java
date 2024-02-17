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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesManagementService {
    private final SalesManagementRepository saleManagementRepository;
    private final PurchaseRepository purchaseRepository;
    public SalesManagement getSalesManagement(Long purchaseId){
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid purchase Id:" + purchaseId));

        Optional<SalesManagement> salesManagement = saleManagementRepository.findBySalesDate(String.valueOf(purchase.getPurchaseDate()));

        if (salesManagement.isPresent()){
            return salesManagement.get();
        } else {
            int staerNum = 1;
            SalesManagement createSalesManagement = new SalesManagement();
            LocalDate date = purchase.getPurchaseDate();

            createSalesManagement.setSalesDate(String.valueOf(date));
            createSalesManagement.setDailyTotalSales(purchase.getPurchaseTotal());
            createSalesManagement.setDailySalesNumber(staerNum);

            saleManagementRepository.save(createSalesManagement);

            return createSalesManagement;
        }
    }
    public List<SalesManagement> printTotalSales(Long purchaseId){
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid purchase Id:" + purchaseId));

        SalesManagement salesManagement = getSalesManagement(purchaseId);
        LocalDate date = purchase.getPurchaseDate();

        // 일간 매출
        Long dailySales = purchaseRepository.getSalesByDate(date);
        int dailyNum = purchaseRepository.getCountByDate(date);

        // 주간 매출
        Long weeklySales = printSalesOfWeek(purchase);
        int year = date.getYear();
        int week = (int) WeekFields.of(DayOfWeek.MONDAY, 1).weekOfYear().getFrom(date);
        int weekNum = purchaseRepository.getCountByWeek(year, week);

        // 월간 매출
        Long monthlySales = printMonthlySales(purchase);
        int monthYear = date.getYear();
        int month = date.getMonthValue();
        int monthNum = purchaseRepository.getCountByMonth(monthYear, month);

        // 저장
        salesManagement.setSalesDate(String.valueOf(date));
        salesManagement.setDailyTotalSales(dailySales);
        salesManagement.setWeekTotalSales(weeklySales);
        salesManagement.setMonthTotalSales(monthlySales);
        salesManagement.setDailySalesNumber(dailyNum);
        salesManagement.setWeekSalesNumber(weekNum);
        salesManagement.setMonthSalesNumber(monthNum);

        saleManagementRepository.save(salesManagement);

        List<SalesManagement> salesManagementList = saleManagementRepository.findAll();

        return salesManagementList;
    }

//    public SalesManagement getSalesManagement(Purchase purchase){
//        Optional<SalesManagement> salesManagement = saleManagementRepository.findBySalesDate(String.valueOf(purchase.getPurchaseDate()));
//
//        if (salesManagement.isPresent()){
//            return salesManagement.get();
//        } else {
//            int staerNum = 1;
//            SalesManagement createSalesManagement = new SalesManagement();
//            LocalDate date = purchase.getPurchaseDate();
//
//            createSalesManagement.setSalesDate(String.valueOf(date));
//            createSalesManagement.setDailyTotalSales(purchase.getPurchaseTotal());
//            createSalesManagement.setDailySalesNumber(staerNum);
//
//            saleManagementRepository.save(createSalesManagement);
//
//            return createSalesManagement;
//        }
//    }

//    public SalesManagement printTotalSales(Purchase purchase){
//        SalesManagement salesManagement = getSalesManagement(purchase);
//        LocalDate date = purchase.getPurchaseDate();
//        // 일간 매출
//        Long dailySales = purchaseRepository.getSalesByDate(date);
//        int dailyNum = purchaseRepository.getCountByDate(date);
//
//        // 주간 매출
//        Long weeklySales = printSalesOfWeek(purchase);
//        int year = date.getYear();
//        int week = (int) WeekFields.of(DayOfWeek.MONDAY, 1).weekOfYear().getFrom(date);
//        int weekNum = purchaseRepository.getCountByWeek(year, week);
//
//        // 월간 매출
//        Long monthlySales = printMonthlySales(purchase);
//        int monthYear = date.getYear();
//        int month = date.getMonthValue();
//        int monthNum = purchaseRepository.getCountByMonth(monthYear, month);
//
//        // 저장
//        salesManagement.setSalesDate(String.valueOf(date));
//        salesManagement.setDailyTotalSales(dailySales);
//        salesManagement.setWeekTotalSales(weeklySales);
//        salesManagement.setMonthTotalSales(monthlySales);
//        salesManagement.setDailySalesNumber(dailyNum);
//        salesManagement.setWeekSalesNumber(weekNum );
//        salesManagement.setMonthSalesNumber(monthNum);
//
//        saleManagementRepository.save(salesManagement);
//
//        return salesManagement;
//    }
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
        int month = date.getMonthValue();
        Long monthlySales = purchaseRepository.getSalesByMonth(year, month);
        return monthlySales;
    }
}
