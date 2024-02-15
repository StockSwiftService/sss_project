package org.example.stockswiftservice.domain.stock.service;

import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.stock.entity.Stock;
import org.example.stockswiftservice.domain.stock.repository.StockRepository;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;

    public List<Stock> getList() {
        return this.stockRepository.findAll();
    }

    public Stock getStock(Long id) {
        Optional<Stock> optionalStock = this.stockRepository.findById(id);

        if (optionalStock.isEmpty()) {
            throw  new RuntimeException("없음");
        }

        return optionalStock.get();
    }

    public RsData<Stock> create(String itemCode, String itemName, String transactionDate, String clientName,
                                Long quantity, Long unitPrice,Long totalAmount){
        Stock stock = Stock.builder()
                .itemCode(itemCode)
                .itemName(itemName)
                .transactionDate(transactionDate)
                .clientName(clientName)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .totalAmount(totalAmount)
                .build();

        stockRepository.save(stock);

        return RsData.of("S-3", "재고 생성 성공", stock);
    }

    public RsData<Stock> modify(Stock stock, String itemCode, String itemName, String transactionDate, String clientName,
                                Long quantity, Long unitPrice, Long totalAmount) {
        stock.setItemCode(itemCode);
        stock.setItemName(itemName);
        stock.setTransactionDate(transactionDate);
        stock.setClientName(clientName);
        stock.setQuantity(quantity);
        stock.setUnitPrice(unitPrice);
        stock.setTotalAmount(totalAmount);

        stockRepository.save(stock);

        return RsData.of("S-4", "재고 수정 성공", stock);
    }

    public RsData<Stock> delete(Stock stock) {
        this.stockRepository.delete(stock);
        return RsData.of("S-5", "재고 삭제 성공", stock);

    }
}
