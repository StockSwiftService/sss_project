package org.example.stockswiftservice;

import org.example.stockswiftservice.domain.client.service.ClientService;
import org.example.stockswiftservice.domain.stock.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockSwiftServiceApplicationTests {

    @Autowired
    private ClientService clientService;

    @Autowired
    private StockService stockService;
    @Test
    void contextLoads() {
        clientService.create("삼성전자", "이재용", "01011111111", "서울특별시 강북구 화곡로 151-3 삼성빌딩 5층");
        clientService.create("현대", "김용우", "01022222222", "서울특별시 강남구 강남로 154-5");
    }

    @Test
    void contextLoads2() {
//        stockService.create("네모주식회사", "네모스낵 500g", 50L, 15000L, 20000L);
//        stockService.create("네모주식회사", "네모사탕 30g", 100L, 500L, 1500L);
        stockService.create("세모주식회사", "세모젤리 50g", 100L, 800L, 2300L);
        stockService.create("영심이분식", "떡만튀 밀키트 500g", 50L, 3000L, 5400L);
    }

}
