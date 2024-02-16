package org.example.stockswiftservice.domain.purchase.service;

import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;


    public void printDailySales(){
        LocalDate date = LocalDate.of(2024, 2, 15);
        Long dailySales = purchaseRepository.getSalesByDate(date);
        System.out.println("2024년 2월 15일의 매출: " + dailySales);
    }

    public Long printSalesOfWeek(LocalDate date){
        int year = date.getYear();
        int week = (int) WeekFields.of(DayOfWeek.MONDAY, 1).weekOfYear().getFrom(date);
        Long weeklySales = purchaseRepository.getSalesByWeek(year, week);
        System.out.println(year + "년 " + date.getMonthValue() + "월 " + week + "번째 주의 매출: " + weeklySales);
        return weeklySales;
    }

    public void printMonthlySales(){
        int year = 2024;
        int month = 2;
        Long monthlySales = purchaseRepository.getSalesByMonth(year, month);
        System.out.println("2024년 2월의 매출: " + monthlySales);
    }
    public Purchase create(Long pur, String dateString){
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        LocalDate date = LocalDate.parse(dateString);

        calendar.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());

        Purchase purchase = Purchase.builder()
                .purchaseTotal(pur)
                .purchaseDate(date)
                .build();
        return purchase;
    }

    public Purchase getPurchase(Long id){
       Optional<Purchase> purchase = purchaseRepository.findById(id);

       return purchase.get();
    }
}
