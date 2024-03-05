package org.example.stockswiftservice.domain.sale.service;

import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.sale.entity.Sale;
import org.example.stockswiftservice.domain.sale.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;

    public void create(String date, Long allSalePrice){
        Sale sale = Sale.builder()
                .saleDate(LocalDate.parse(date))
                .allSalePrice(allSalePrice)
                .build();

        saleRepository.save(sale);
    }


}
