package org.example.stockswiftservice.domain.stock.service;

import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.stock.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
}
