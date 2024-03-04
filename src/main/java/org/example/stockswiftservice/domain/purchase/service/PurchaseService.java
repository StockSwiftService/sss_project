package org.example.stockswiftservice.domain.purchase.service;

import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.entity.PurchaseStock;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseRepository;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseStockRepository;
import org.example.stockswiftservice.domain.stock.entity.Stock;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.global.rs.RsData;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseStockRepository purchaseStockRepository;
    public List<Purchase> getList() {
        return this.purchaseRepository.findAllByApprovalFalse();
    }

    public List<Purchase> getApprovalList() {
        return this.purchaseRepository.findAllByApprovalTrue();
    }

    public RsData<Purchase> create(LocalDate purchaseDate, Client selectedClient, Boolean deliveryStatus, String significant, List<PurchaseStock> filteredItems, Long allPrice) {
        Purchase purchase = Purchase.builder()
                .purchaseDate(purchaseDate)
                .client(selectedClient)
                .deliveryStatus(deliveryStatus)
                .significant(significant)
                .allPrice(allPrice)
                .build();

        purchaseRepository.save(purchase);

        for (PurchaseStock item : filteredItems) {
            item.setPurchase(purchase);
            purchaseStockRepository.save(item);
        }

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

    public void approval(List<Long> ids) {
        for (Long id : ids) {
            Optional<Purchase> optionalPurchase = this.purchaseRepository.findById(id);
            Purchase purchase = optionalPurchase.get();
            purchase.setApproval(true);
            this.purchaseRepository.save(purchase);
        }
    }

    public void approvalCancel(List<Long> ids) {
        for (Long id : ids) {
            Optional<Purchase> optionalPurchase = this.purchaseRepository.findById(id);
            Purchase purchase = optionalPurchase.get();
            purchase.setApproval(false);
            this.purchaseRepository.save(purchase);
        }
    }

    public void delete(List<Long> ids) {
        for (Long id : ids) {
            Optional<Purchase> optionalPurchase = this.purchaseRepository.findById(id);
            Purchase purchase = optionalPurchase.get();
            purchaseRepository.delete(purchase);
        }
    }

    public Map<LocalDate, List<Purchase>> getApprovedPurchasesGroupedByDate() {
        return purchaseRepository.findAllByApprovalTrue().stream()
                .collect(Collectors.groupingBy(Purchase::getPurchaseDate,
                        TreeMap::new,
                        Collectors.toList()));
    }
}
