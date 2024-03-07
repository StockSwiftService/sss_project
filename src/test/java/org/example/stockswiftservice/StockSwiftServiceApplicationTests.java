package org.example.stockswiftservice;

import org.example.stockswiftservice.domain.client.service.ClientService;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseRepository;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseStockRepository;
import org.example.stockswiftservice.domain.purchase.service.PurchaseService;
import org.example.stockswiftservice.domain.question.service.QuestionService;
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
    private PurchaseStockRepository purchaseStockRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private StockService stockService;

    @Autowired
    private QuestionService questionService;

    String companyCode1 = "VWTRPA";
    String companyCode2 = "MWEKSE";

    @Test
    void clientTest1() {
        clientService.create(companyCode1,"한국식품유통", "이지훈", "01031245742", "대전 중구 계룡로816번길 32-4", "105호");
        clientService.create(companyCode1,"서울농산물", "김민수", "01082716345", "서울특별시 강남구 테헤란로 152", "201호");
        clientService.create(companyCode1,"부산수산유통", "박지영", "01046281729", "부산광역시 해운대구 센텀2로 25", "301호");
        clientService.create(companyCode1,"인천냉동식품", "정수빈", "01058319427", "인천광역시 남동구 정각로 29", "402호");
        clientService.create(companyCode1,"광주청과", "이현우", "01073925148", "광주광역시 서구 치평로 20", "503호");
        clientService.create(companyCode1,"대구식자재마트", "최유나", "01089237456", "대구광역시 중구 달구벌대로 2222", "604호");
        clientService.create(companyCode1,"울산건강식품", "황도윤", "01031725849", "울산광역시 남구 문수로 85", "705호");
        clientService.create(companyCode1,"세종농수산", "김태리", "01082615937", "세종특별자치시 도움8로 15", "806호");
        clientService.create(companyCode1,"제주도식품유통", "정민재", "01049271638", "제주특별자치도 제주시 첨단로 242", "907호");
        clientService.create(companyCode1,"청주식자재", "한지원", "01068721395", "충청북도 청주시 테크노폴리스로 123", "1008호");
    }

    @Test
    void stockTest1() {
        stockService.create(companyCode1,"청주식자재", "국물 떡볶이 200g", 200L, 200L, 2800L, 5500L);
        stockService.create(companyCode1,"청주식자재", "로제 떡볶이 200g", 200L, 200L, 3500L, 6500L);
        stockService.create(companyCode1,"한국식품유통", "아이스 아메리카노 스틱 100T", 300L, 300L, 10100L, 15300L);
        stockService.create(companyCode1,"한국식품유통", "바닐라 라떼 스틱 50T", 200L, 200L, 20200L, 28000L);
        stockService.create(companyCode1,"서울농산물", "백미 20kg", 100L, 100L, 25000L, 38000L);
        stockService.create(companyCode1,"서울농산물", "현미 10kg", 50L, 50L, 18000L, 25000L);
        stockService.create(companyCode1,"부산수산유통", "쥐포 500g", 200L,200L, 15000L, 23000L);
        stockService.create(companyCode1,"부산수산유통", "오징어 다리 200g", 300L,300L, 8000L, 12000L);
        stockService.create(companyCode1,"인천냉동식품", "군만두 500g", 200L,200L, 6000L, 9000L);
        stockService.create(companyCode1,"인천냉동식품", "치킨 팝콘 500g", 300L,300L, 7000L, 10000L);
        stockService.create(companyCode1,"광주청과", "세척 사과 1kg", 200L,200L, 20000L, 30000L);
        stockService.create(companyCode1,"광주청과", "샤인 머스켓 1kg", 100L,100L, 40000L, 55000L);
        stockService.create(companyCode1,"대구식자재마트", "윙봉 1kg", 300L,300L, 10000L, 15000L);
        stockService.create(companyCode1,"대구식자재마트", "매운 핫바 1kg", 200L,200L, 15000L, 20000L);
        stockService.create(companyCode1,"제주도식품유통", "감귤 초콜렛 500g", 300L, 300L, 5000L, 8000L);
        stockService.create(companyCode1,"제주도식품유통", "감귤 크래커 500g", 200L, 200L, 7000L, 10000L);
    }

    @Test
    void clientTest2() {
        clientService.create(companyCode2, "글로벌 문구공급", "김영희", "01012345678", "서울 강남구 테헤란로 123", "301호");
        clientService.create(companyCode2, "펜과지우개 주식회사", "이철수", "01098765432", "경기 성남시 분당구 판교로 234", "502호");
        clientService.create(companyCode2, "종이나라", "박민지", "01055557777", "서울 영등포구 여의대방로 345", "701호");
        clientService.create(companyCode2, "문구마켓", "홍길동", "01033334444", "인천 남동구 공항대로 456", "202호");
        clientService.create(companyCode2, "펜타운", "신영수", "01066669999", "대구 수성구 동대구로 567", "402호");
        clientService.create(companyCode2, "노트메이커", "김철호", "01022228888", "부산 해운대구 해운대로 678", "802호");
        clientService.create(companyCode2, "글로벌스테이션", "장미영", "01044442222", "광주 북구 첨단과기로 789", "102호");
        clientService.create(companyCode2, "문구올림픽", "정명호", "01077775555", "대전 서구 둔산로 890", "502호");
        clientService.create(companyCode2, "펜파크", "이지수", "01088883333", "울산 남구 삼산로 901", "303호");
        clientService.create(companyCode2, "노트코리아", "최영희", "01099990000", "세종특별자치시 한누리대로 101", "105호");
    }

    @Test
    void stockTest2() {
        stockService.create(companyCode2,"글로벌 문구공급", "연필 3다스", 150L, 150L, 2500L, 4800L);
        stockService.create(companyCode2,"펜과지우개 주식회사", "볼펜 5다스", 200L, 200L, 3000L, 5500L);
        stockService.create(companyCode2,"종이나라", "노트 10다스", 100L, 100L, 5000L, 9000L);
        stockService.create(companyCode2,"문구마켓", "스티커 2팩", 300L, 300L, 1000L, 2000L);
        stockService.create(companyCode2,"펜타운", "클립 8박스", 80L, 80L, 800L, 1500L);
        stockService.create(companyCode2,"노트메이커", "메모지 15팩", 120L, 120L, 2000L, 3500L);
        stockService.create(companyCode2,"글로벌스테이션", "책갈피 4세트", 150L, 150L, 1200L, 2200L);
        stockService.create(companyCode2,"문구올림픽", "수정테이프 20개", 250L, 250L, 1500L, 2800L);
        stockService.create(companyCode2,"펜파크", "파일 10개", 180L, 180L, 1800L, 3200L);
        stockService.create(companyCode2,"노트코리아", "연필 2다스", 180L, 180L, 2000L, 3800L);
    }

    @Test
    void questionTest() {
        questionService.createQuestion("질문1", "내용1", true);
        questionService.createQuestion("질문2", "내용2", true);
        questionService.createQuestion("질문3", "내용3", true);
        questionService.createQuestion("질문4", "내용4", true);
        questionService.createQuestion("질문5", "내용5", true);
    }
}