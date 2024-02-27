package org.example.stockswiftservice.domain.purchase.service;

import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.entity.PurchaseStock;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseRepository;
import org.example.stockswiftservice.domain.stock.entity.Stock;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.global.rs.RsData;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;

    public List<Purchase> getList() {
        return this.purchaseRepository.findAll();
    }

    public RsData<Purchase> create(LocalDate purchaseDate, Client selectedClient, Boolean deliveryStatus, String significant, List<PurchaseStock> items, Long allPrice) {
        Purchase purchase = Purchase.builder()
                .purchaseDate(purchaseDate)
                .client(selectedClient)
                .deliveryStatus(deliveryStatus)
                .significant(significant)
                .purchaseStocks(items)
                .allPrice(allPrice)
                .build();

        purchaseRepository.save(purchase);

        return RsData.of("1", "판매 등록 완료", purchase);
    }

    public Purchase getPurchase(Long id){
       Optional<Purchase> purchase = purchaseRepository.findById(id);

       return purchase.get();
    }

    public List<Purchase> getPurchaseList(String dateString){
        LocalDate date = LocalDate.parse(dateString);

        List<Purchase> getPurchaseList = purchaseRepository.findByPurchaseDate(date);

        return getPurchaseList;
    }
}
