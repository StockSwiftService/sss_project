package org.example.stockswiftservice;

import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.service.PurchaseService;
import org.example.stockswiftservice.domain.salemanagement.entity.SalesManagement;
import org.example.stockswiftservice.domain.salemanagement.repository.SalesManagementRepository;
import org.example.stockswiftservice.domain.salemanagement.service.SalesManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockSwiftServiceApplicationTests {
    @Autowired
    private SalesManagementRepository salesManagementRepository;

    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private SalesManagementService salesManagementService;
    @Test
    void contextLoads() {
        Purchase purchase = purchaseService.getPurchase(4L);
        String purchaseDate = String.valueOf(purchase.getPurchaseDate());

//        Optional<SalesManagemant> optionalSalesManagement = salesManagementRepository.findBySalesDate(purchaseDate);
//
//        if (optionalSalesManagement.isPresent()){
//            System.out.println(1);
//        }else {
//            System.out.println(0);
//        }

        SalesManagement salesManagemant = salesManagementService.printTotalSales(purchase);
//        System.out.println(salesManagemant);
    }
}
