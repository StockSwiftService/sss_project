package org.example.stockswiftservice;

import org.example.stockswiftservice.domain.purchase.repository.PurchaseRepository;
import org.example.stockswiftservice.domain.purchase.service.PurchaseService;
import org.example.stockswiftservice.domain.salemanagement.service.SalesManagementService;
import org.junit.jupiter.api.DisplayName;
import org.example.stockswiftservice.domain.client.service.ClientService;
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
    void clientTest() {
        clientService.create("이니시스", "이지훈", "01031245742", "대전 중구 계룡로816번길 32-4");
        clientService.create("다음카카오", "김범수", "01045678901", "제주특별자치도 제주시 첨단로 242");
        clientService.create("네이버", "이해진", "01098765432", "경기도 성남시 분당구 불정로 6");
        clientService.create("삼성전자", "김기남", "01012345678", "서울특별시 서초구 서초대로 74길 11");
        clientService.create("LG전자", "권봉석", "01087654321", "서울특별시 영등포구 여의대로 128");
        clientService.create("SK하이닉스", "이석희", "01056789012", "경기도 이천시 부발읍 경충대로 2091");
        clientService.create("현대자동차", "정의선", "01034567890", "서울특별시 서초구 헌릉로 12");
        clientService.create("기아자동차", "송호성", "01023456789", "서울특별시 서초구 양재대로 12길 37");
        clientService.create("포스코", "최정우", "01078901234", "경상북도 포항시 남구 포스코대로 1");
        clientService.create("롯데케미칼", "김교현", "01089012345", "서울특별시 송파구 올림픽로 300");
        clientService.create("한화솔루션", "김동관", "01090123456", "서울특별시 중구 청계천로 1");
        clientService.create("CJ제일제당", "신현재", "01067890123", "서울특별시 중구 을지로 100");
        clientService.create("두산중공업", "박지원", "01078901234", "경상남도 창원시 성산구 두산볼보로 22");
        clientService.create("LG화학", "신학철", "01089012345", "서울특별시 영등포구 여의나루로 4");
        clientService.create("셀트리온", "서정진", "01090123456", "인천광역시 연수구 송도과학로 85");
        clientService.create("넥슨", "김정주", "01012345678", "경기도 성남시 분당구 판교로 256번길 7");
        clientService.create("NC소프트", "김택진", "01023456789", "경기도 성남시 분당구 판교로 256번길 12");
        clientService.create("넷마블", "방준혁", "01034567890", "서울특별시 구로구 디지털로 300");
        clientService.create("카카오", "여민수", "01045678901", "제주특별자치도 제주시 첨단로 242");
        clientService.create("우아한형제들", "김봉진", "01056789012", "서울특별시 송파구 올림픽로 300");
        clientService.create("야놀자", "이수진", "01067890123", "서울특별시 중구 퇴계로 123");
    }

    @Test
    void contextLoads2() {
        stockService.create("우아한형제들", "소고기스톡 200g", 300L, 4000L, 6000L);
        stockService.create("마켓컬리", "야채스톡 200g", 200L, 2000L, 10000L);
        stockService.create("마켓컬리", "포크스톡 200g", 800L, 4000L, 10000L);
        stockService.create("마켓컬리", "포크스톡 200g", 1000L, 1000L, 10000L);
        stockService.create("롯데마트", "소고기스톡 200g", 800L, 2000L, 9000L);
        stockService.create("이마트", "해물스톡 200g", 200L, 1000L, 8000L);
        stockService.create("우아한형제들", "비프스톡 200g", 900L, 1000L, 9000L);
        stockService.create("우아한형제들", "소고기스톡 200g", 1000L, 2000L, 7000L);
        stockService.create("홈플러스", "포크스톡 200g", 800L, 3000L, 10000L);
        stockService.create("홈플러스", "야채스톡 200g", 500L, 4000L, 10000L);
        stockService.create("쿠팡", "포크스톡 200g", 100L, 3000L, 10000L);
        stockService.create("우아한형제들", "비프스톡 200g", 700L, 5000L, 7000L);
        stockService.create("롯데마트", "치킨스톡 200g", 100L, 4000L, 10000L);
        stockService.create("롯데마트", "비프스톡 200g", 800L, 1000L, 10000L);
        stockService.create("우아한형제들", "비프스톡 200g", 200L, 1000L, 7000L);
        stockService.create("홈플러스", "포크스톡 200g", 100L, 4000L, 7000L);
        stockService.create("홈플러스", "야채스톡 200g", 300L, 1000L, 7000L);
        stockService.create("우아한형제들", "소고기스톡 200g", 800L, 2000L, 9000L);
        stockService.create("롯데마트", "해물스톡 200g", 1000L, 4000L, 10000L);
        stockService.create("이마트", "포크스톡 200g", 100L, 1000L, 10000L);
        stockService.create("쿠팡", "치킨스톡 200g", 200L, 5000L, 8000L);
        stockService.create("쿠팡", "포크스톡 200g", 300L, 3000L, 6000L);
        stockService.create("이마트", "포크스톡 200g", 800L, 1000L, 7000L);
        stockService.create("이마트", "비프스톡 200g", 1000L, 2000L, 10000L);
        stockService.create("우아한형제들", "치킨스톡 200g", 1000L, 1000L, 10000L);
        stockService.create("이마트", "포크스톡 200g", 1000L, 5000L, 8000L);
    }

    @Test
    @DisplayName("데이터 밀어넣기")
    void createPur(){
        purchaseService.create(6543123L, "2023-12-17");
        purchaseService.create(1234553L, "2023-12-01");
        purchaseService.create(12353L, "2023-11-11");
        purchaseService.create(1200000L, "2024-02-14");
        purchaseService.create(1200123L, "2024-02-14");
        purchaseService.create(360000L, "2024-02-14");
        purchaseService.create(1200000L, "2024-02-15");
        purchaseService.create(200000L, "2024-02-16");
        purchaseService.create(800000L, "2024-03-02");
        purchaseService.create(800000L, "2024-03-10");
        purchaseService.create(8213430L, "2024-03-27");
        purchaseService.create(800000L, "2024-04-26");
        purchaseService.create(800000L, "2025-01-20");
        purchaseService.create(12354444L, "2023-11-22");
    }
}
