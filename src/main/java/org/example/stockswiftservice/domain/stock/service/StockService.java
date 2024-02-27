package org.example.stockswiftservice.domain.stock.service;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.domain.client.repository.ClientRepository;
import org.example.stockswiftservice.domain.stock.entity.Stock;
import org.example.stockswiftservice.domain.stock.repository.StockRepository;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final ClientRepository clientRepository;

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
        Client client = clientRepository.findByClientName(clientName)
                .orElseThrow(() -> new RuntimeException("클라이언트를 찾을 수 없습니다."));
        Stock stock = new Stock();
                stock.setClient(client);
                stock.setItemName(itemName);
                stock.setQuantity(quantity);
                stock.setPurchasePrice(purchasePrice);
                stock.setSalesPrice(salesPrice);
                stock.setCreateDate(LocalDateTime.now());

        stockRepository.save(stock);

        return RsData.of("S-3", "재고 생성 성공", stock);
    }

    public RsData<Stock> modify(Stock stock, String clientName, String itemName, Long purchasePrice, Long salesPrice) {
        Client client = clientRepository.findByClientName(clientName)
                .orElseThrow(() -> new RuntimeException("클라이언트를 찾을 수 없습니다."));

        stock.setClient(client);
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

    public Optional<Stock> findByItemName(String clientName, String itemName) {
        return stockRepository.findByItemName(clientName, itemName);
    }

    private Specification<Stock> search(String kw) {
        return new Specification<Stock>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Stock> a, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                Join<Stock, Client> clientJoin = a.join("client");
                return cb.or(
                        cb.like(clientJoin.get("clientName"), "%" + kw + "%"),
                        cb.like(a.get("itemName"), "%" + kw + "%")
                );
            }
        };
    }
}