package org.example.stockswiftservice.domain.purchase.service;

import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;

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
