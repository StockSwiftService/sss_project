package org.example.stockswiftservice;

import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseRepository;
import org.example.stockswiftservice.domain.purchase.service.PurchaseService;
import org.example.stockswiftservice.domain.salemanagement.service.SalesManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class StockSwiftServiceApplicationTests {
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private SalesManagementService salesManagementService;
    @Test
    void contextLoads() {
        Purchase purchase = purchaseService.getPurchase(7L);
        String purchaseDate = String.valueOf(purchase.getPurchaseDate());

        LocalDate date = purchase.getPurchaseDate();

        salesManagementService.printTotalSales(1L);
//        System.out.println(salesManagemant);
    }
}
