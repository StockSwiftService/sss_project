package org.example.stockswiftservice.domain.purchase.service;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.domain.client.repository.ClientRepository;
import org.example.stockswiftservice.domain.company.entity.Company;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.entity.PurchaseStock;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseRepository;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseStockRepository;
import org.example.stockswiftservice.domain.stock.entity.Stock;
import org.example.stockswiftservice.domain.stock.repository.StockRepository;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseStockRepository purchaseStockRepository;
    private final StockRepository stockRepository;
    private final ClientRepository clientRepository;
    public List<Purchase> getList() {
        return this.purchaseRepository.findAll();
    }

    public Page<Purchase> getSearchList(String kw, int page, boolean whether, String companyCode) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 6, Sort.by(sorts));
        Specification<Purchase> spec = search(kw, whether, companyCode);
        return this.purchaseRepository.findAll(spec, pageable);
    }

    private Specification<Purchase> search(String kw, boolean whether, String companyCode) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<Purchase, PurchaseStock> purchaseStockJoin = root.join("purchaseStocks", JoinType.LEFT);
            Path<String> itemNamePath = purchaseStockJoin.get("itemName");

            // 기존 검색 조건
            Predicate searchPredicate = cb.or(
                    cb.like(root.get("significant"), "%" + kw + "%"),
                    cb.like(root.join("client").get("clientName"), "%" + kw + "%"),
                    cb.like(itemNamePath, "%" + kw + "%")
            );

            // approval 필드가 whether 값과 일치하는 조건
            Predicate approvalPredicate = cb.equal(root.get("approval"), whether);

            // companyCode가 파라미터로 받은 companyCode와 일치하는 조건
            Predicate companyCodePredicate = cb.equal(root.get("companyCode"), companyCode);

            // 최종 조건: 검색 조건, approval 조건, 그리고 companyCode 조건을 AND 연산으로 결합
            return cb.and(searchPredicate, approvalPredicate, companyCodePredicate);
        };
    }

    public RsData<Purchase> create(String companyCode, LocalDate purchaseDate, Client selectedClient, Boolean deliveryStatus, String significant, List<PurchaseStock> filteredItems, Long allPrice) {
        Purchase purchase = Purchase.builder()
                .companyCode(companyCode)
                .purchaseDate(purchaseDate)
                .client(selectedClient)
                .deliveryStatus(deliveryStatus)
                .significant(significant)
                .allPrice(allPrice)
                .approval(false)
                .build();

        purchaseRepository.save(purchase);

        for (PurchaseStock item : filteredItems) {
            item.setPurchase(purchase);
            purchaseStockRepository.save(item);
        }

        return RsData.of("1", "판매 등록 완료", purchase);
    }

    public List<Purchase> approval(List<Long> ids) {
        List<Purchase> purchases = new ArrayList<>();
        for (Long id : ids) {
            Optional<Purchase> optionalPurchase = this.purchaseRepository.findById(id);
            Purchase purchase = optionalPurchase.get();
            purchase.setApproval(true);

            List<PurchaseStock> purchaseStocks = purchase.getPurchaseStocks();
            for (PurchaseStock purchaseStock : purchaseStocks) {
                Optional<Stock> optionalStock = this.stockRepository.findByItemName(purchaseStock.getItemName());
                Stock stock = optionalStock.get();
                stock.setQuantity(optionalStock.get().getQuantity() - purchaseStock.getInputQuantity());
                this.stockRepository.save(stock);
            }

            this.purchaseRepository.save(purchase);

            purchases.add(purchase);
        }

        return purchases;
    }

    public List<Purchase> delete(List<Long> ids) {
        List<Purchase> purchases = new ArrayList<>();
        for (Long id : ids) {
            Optional<Purchase> optionalPurchase = this.purchaseRepository.findById(id);
            Purchase purchase = optionalPurchase.get();
            purchaseRepository.delete(purchase);
            purchases.add(purchase);
        }

        return purchases;
    }

    public Map<LocalDate, List<Purchase>> getApprovedPurchasesByItemNameGroupedByDate(String itemName) {
        return purchaseRepository.findAllByApprovalTrue().stream()
                .filter(purchase -> purchase.getPurchaseStocks().stream()
                        .anyMatch(stock -> stock.getItemName().equals(itemName)))
                .collect(Collectors.groupingBy(Purchase::getPurchaseDate,
                        TreeMap::new,
                        Collectors.toList()));
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

    public RsData<Purchase> modify(Purchase purchase, LocalDate purchaseDate, Client selectedClient, Boolean deliveryStatus, String significant, List<PurchaseStock> filteredItems, Long allPrice) {
        Client client = clientRepository.findByClientName(selectedClient.getClientName())
                .orElseThrow(() -> new RuntimeException("클라이언트를 찾을 수 없습니다."));

            List<PurchaseStock> purchaseStocks = this.purchaseStockRepository.findByPurchaseId(purchase.getId());

            purchaseStockRepository.deleteAll(purchaseStocks);

        purchase.setPurchaseDate(purchaseDate);
        purchase.setClient(client);
        purchase.setDeliveryStatus(deliveryStatus);
        purchase.setSignificant(significant);
        purchase.setAllPrice(allPrice);

        purchaseRepository.save(purchase);

        for (PurchaseStock item : filteredItems) {
            item.setPurchase(purchase);
            purchaseStockRepository.save(item);
        }

        return RsData.of("1", "판매 수정 완료", purchase);
    }
}
