package org.example.stockswiftservice;

import org.example.stockswiftservice.domain.client.service.ClientService;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseRepository;
import org.example.stockswiftservice.domain.purchase.service.PurchaseService;
import org.example.stockswiftservice.domain.salemanagement.service.SalesManagementService;
import org.example.stockswiftservice.domain.stock.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockSwiftServiceApplicationTests {
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private SalesManagementService salesManagementService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private StockService stockService;
    @Test
    void contextLoads() {
        salesManagementService.printTotalSales(1L);
        salesManagementService.printTotalSales(2L);
        salesManagementService.printTotalSales(3L);
        salesManagementService.printTotalSales(4L);
        salesManagementService.printTotalSales(5L);
        salesManagementService.printTotalSales(6L);
        salesManagementService.printTotalSales(7L);
        salesManagementService.printTotalSales(8L);
        salesManagementService.printTotalSales(9L);
        salesManagementService.printTotalSales(10L);
        salesManagementService.printTotalSales(11L);
        salesManagementService.printTotalSales(12L);
        salesManagementService.printTotalSales(13L);
        salesManagementService.printTotalSales(14L);
//        System.out.println(salesManagemant);
    }

//    @Test
//    @DisplayName("데이터 밀어넣기")
//    void createPur(){
//        purchaseService.create(6543123L, "2023-12-17");
//        purchaseService.create(1234553L, "2023-12-01");
//        purchaseService.create(12353L, "2023-11-11");
//        purchaseService.create(1200000L, "2024-02-14");
//        purchaseService.create(1200123L, "2024-02-14");
//        purchaseService.create(360000L, "2024-02-14");
//        purchaseService.create(1200000L, "2024-02-15");
//        purchaseService.create(200000L, "2024-02-16");
//        purchaseService.create(800000L, "2024-03-02");
//        purchaseService.create(800000L, "2024-03-10");
//        purchaseService.create(8213430L, "2024-03-27");
//        purchaseService.create(800000L, "2024-04-26");
//        purchaseService.create(800000L, "2025-01-20");
//        purchaseService.create(12354444L, "2023-11-22");
//    }
}
