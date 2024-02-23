package org.example.stockswiftservice.domain.stock.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.stock.entity.Stock;
import org.example.stockswiftservice.domain.stock.repository.StockRepository;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;

    public List<Stock> getList() {
        return this.stockRepository.findAll();
    }

    public Page<Stock> getSearchList(String kw, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 6, Sort.by(sorts));
        Specification<Stock> spec = search(kw);
        return this.stockRepository.findAll(spec, pageable);
    }

    public Stock getStock(Long id) {
        Optional<Stock> optionalStock = this.stockRepository.findById(id);

        if (optionalStock.isEmpty()) {
            throw  new RuntimeException("없음");
        }

        return optionalStock.get();
    }

    public RsData<Stock> create(String clientName, String itemName, Long quantity, Long purchasePrice, Long salesPrice){
        Stock stock = Stock.builder()
                .clientName(clientName)
                .itemName(itemName)
                .quantity(quantity)
                .purchasePrice(purchasePrice)
                .salesPrice(salesPrice)
                .build();

        stockRepository.save(stock);

        return RsData.of("S-3", "재고 생성 성공", stock);
    }

    public RsData<Stock> modify(Stock stock, String clientName, String itemName, Long purchasePrice, Long salesPrice) {
        stock.setClientName(clientName);
        stock.setItemName(itemName);
        stock.setPurchasePrice(purchasePrice);
        stock.setSalesPrice(salesPrice);

        stockRepository.save(stock);

        return RsData.of("S-4", "재고 수정 성공", stock);
    }

    public RsData<Stock> delete(Stock stock) {
        this.stockRepository.delete(stock);
        return RsData.of("S-5", "재고 삭제 성공", stock);
    }

    public List<Stock> deleteMultiple(List<Long> id) {
        List<Stock> stocksToDelete = stockRepository.findAllById(id);
        stockRepository.deleteAll(stocksToDelete);
        return stocksToDelete;
    }

    public Optional<Stock> findByItemName(String itemName) {
        return stockRepository.findByItemName(itemName);
    }

    private Specification<Stock> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Stock> a, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                return cb.or(
                        cb.like(a.get("clientName"), "%" + kw + "%"),
                        cb.like(a.get("itemName"), "%" + kw + "%")
                );
            }
        };
    }
}
