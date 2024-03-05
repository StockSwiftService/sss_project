package org.example.stockswiftservice;

import org.example.stockswiftservice.domain.client.service.ClientService;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseRepository;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseStockRepository;
import org.example.stockswiftservice.domain.purchase.service.PurchaseService;
import org.example.stockswiftservice.domain.question.service.QuestionService;
import org.example.stockswiftservice.domain.salemanagement.service.SalesManagementService;
import org.example.stockswiftservice.domain.stock.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StockSwiftServiceApplicationTests {
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private SalesManagementService salesManagementService;

    @Autowired
    private PurchaseStockRepository purchaseStockRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private StockService stockService;

    @Autowired
    private QuestionService questionService;
//
//    @Test
//    void clientTest() {
//        clientService.create("한국식품유통", "이지훈", "01031245742", "대전 중구 계룡로816번길 32-4", "105호");
//        clientService.create("서울농산물", "김민수", "01082716345", "서울특별시 강남구 테헤란로 152", "201호");
//        clientService.create("부산수산유통", "박지영", "01046281729", "부산광역시 해운대구 센텀2로 25", "301호");
//        clientService.create("인천냉동식품", "정수빈", "01058319427", "인천광역시 남동구 정각로 29", "402호");
//        clientService.create("광주청과", "이현우", "01073925148", "광주광역시 서구 치평로 20", "503호");
//        clientService.create("대구식자재마트", "최유나", "01089237456", "대구광역시 중구 달구벌대로 2222", "604호");
//        clientService.create("울산건강식품", "황도윤", "01031725849", "울산광역시 남구 문수로 85", "705호");
//        clientService.create("세종농수산", "김태리", "01082615937", "세종특별자치시 도움8로 15", "806호");
//        clientService.create("제주도식품유통", "정민재", "01049271638", "제주특별자치도 제주시 첨단로 242", "907호");
//        clientService.create("청주식자재", "한지원", "01068721395", "충청북도 청주시 테크노폴리스로 123", "1008호");
//    }
//
//    @Test
//    void stockTest() {
//        stockService.create("청주식자재", "국물 떡볶이 200g", 200L, 2800L, 5500L);
//        stockService.create("청주식자재", "로제 떡볶이 200g", 200L, 3500L, 6500L);
//        stockService.create("한국식품유통", "아이스 아메리카노 스틱 100T", 300L, 10100L, 15300L);
//        stockService.create("한국식품유통", "바닐라 라떼 스틱 50T", 200L, 20200L, 28000L);
//        stockService.create("서울농산물", "백미 20kg", 100L, 25000L, 38000L);
//        stockService.create("서울농산물", "현미 10kg", 50L, 18000L, 25000L);
//        stockService.create("부산수산유통", "쥐포 500g", 200L, 15000L, 23000L);
//        stockService.create("부산수산유통", "오징어 다리 200g", 300L, 8000L, 12000L);
//        stockService.create("인천냉동식품", "군만두 500g", 200L, 6000L, 9000L);
//        stockService.create("인천냉동식품", "치킨 팝콘 500g", 300L, 7000L, 10000L);
//        stockService.create("광주청과", "세척 사과 1kg", 200L, 20000L, 30000L);
//        stockService.create("광주청과", "샤인 머스켓 1kg", 100L, 40000L, 55000L);
//        stockService.create("대구식자재마트", "윙봉 1kg", 300L, 10000L, 15000L);
//        stockService.create("대구식자재마트", "매운 핫바 1kg", 200L, 15000L, 20000L);
//        stockService.create("제주도식품유통", "감귤 초콜렛 500g", 300L, 5000L, 8000L);
//        stockService.create("제주도식품유통", "감귤 크래커 500g", 200L, 7000L, 10000L);
//    }
//
//    @Test
//    void questionTest() {
//        questionService.createQuestion("질문1", "내용1", true);
//        questionService.createQuestion("질문2", "내용2", true);
//        questionService.createQuestion("질문3", "내용3", true);
//        questionService.createQuestion("질문4", "내용4", true);
//        questionService.createQuestion("질문5", "내용5", true);
//    }

    @Test
    void testCase(){
        List<Purchase> all = this.purchaseService.getApprovalList();
        assertEquals(2, all.size());

        Purchase q = all.get(0);
        assertEquals("qwergfh", q.getSignificant());

        Purchase q1 = all.get(1);
        assertEquals("QSadsf", q1.getSignificant());
    }
}