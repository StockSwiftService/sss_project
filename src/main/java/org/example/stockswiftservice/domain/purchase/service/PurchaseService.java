package org.example.stockswiftservice.domain.purchase.service;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.entity.PurchaseStock;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseRepository;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseStockRepository;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.stockswiftservice.global.rs.RsData;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseStockRepository purchaseStockRepository;
    public List<Purchase> getList() {
        return this.purchaseRepository.findAllByApprovalFalse();
    }

    public Page<Purchase> getSearchList(String kw, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 6, Sort.by(sorts));
        Specification<Purchase> spec = search(kw);
        return this.purchaseRepository.findAllByApprovalFalse(spec, pageable);
    }

    private Specification<Purchase> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Purchase> a, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                Join<Purchase, PurchaseStock> purchaseStockJoin = a.join("purchaseStocks", JoinType.LEFT);
                Path<String> itemNamePath = purchaseStockJoin.get("itemName");
                return cb.or(
                        cb.like(a.get("significant"), "%" + kw + "%"),
                        cb.like(a.join("client").get("clientName"), "%" + kw + "%"),
                        cb.like(itemNamePath, "%" + kw + "%")
                );
            }
        };
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
}
