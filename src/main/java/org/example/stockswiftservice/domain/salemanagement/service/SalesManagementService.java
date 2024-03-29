package org.example.stockswiftservice.domain.salemanagement.service;

import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseRepository;
import org.example.stockswiftservice.domain.salemanagement.entity.SalesManagement;
import org.example.stockswiftservice.domain.salemanagement.repository.SalesManagementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static java.util.Locale.KOREA;

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
            Calendar calendar = Calendar.getInstance(KOREA);
            calendar.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
            int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);

            createSalesManagement.setSalesDate(String.valueOf(date));
            createSalesManagement.setDailyTotalSales(purchase.getAllPrice());
            createSalesManagement.setDailySalesNumber(staerNum);
            createSalesManagement.setWeekDate(weekOfMonth);

            saleManagementRepository.save(createSalesManagement);

            return createSalesManagement;
        }
    }
    public List<SalesManagement> printTotalSales(Long purchaseId){
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid purchase Id:" + purchaseId));
        Calendar calendar = Calendar.getInstance(KOREA);

        SalesManagement salesManagement = getSalesManagement(purchaseId);
        LocalDate date = purchase.getPurchaseDate();

        calendar.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
        int dateYear = calendar.get(Calendar.YEAR);
        int dateMonth = calendar.get(Calendar.MONTH) + 1;

        // 일간 매출
        Long dailySales = purchaseRepository.getSalesByDate(date);
        int dailyNum = purchaseRepository.getCountByDate(date);

        // 주간 매출
        Long weeklySales = printSalesOfWeek(purchase);
        int weekNum = purchaseRepository.getCountByWeek(date);

        // 월간 매출
        Long monthlySales = printMonthlySales(purchase);
        int monthYear = date.getYear();
        int month = date.getMonthValue();
        int monthNum = purchaseRepository.getCountByMonth(monthYear, month);

        // 저장
        salesManagement.setSalesDate(String.valueOf(date));
        salesManagement.setWeekDate(weekOfMonth);
        salesManagement.setYear(dateYear);
        salesManagement.setMonth(dateMonth);
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
    public Long printSalesOfWeek(Purchase purchase){
        LocalDate date = purchase.getPurchaseDate();
        Long weeklySales = purchaseRepository.getSalesByWeek(date);
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
