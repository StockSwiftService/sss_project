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

    public RsData<Stock> modify(Stock stock, String clientName, String itemName, Long quantity, Long purchasePrice, Long salesPrice) {
        stock.setClientName(clientName);
        stock.setItemName(itemName);
        stock.setQuantity(quantity);
        stock.setPurchasePrice(purchasePrice);
        stock.setSalesPrice(salesPrice);

        stockRepository.save(stock);

        return RsData.of("S-4", "재고 수정 성공", stock);
    }

    public RsData<Stock> delete(Stock stock) {
        this.stockRepository.delete(stock);
        return RsData.of("S-5", "재고 삭제 성공", stock);

    }
}
