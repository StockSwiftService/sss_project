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

    String companyCode1 = "VNUJJI";
    String companyCode2 = "YEQGZC";

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
        questionService.createQuestion("회원가입을 했는데, 언제 결과가 나오나요?", "알림에 표기했듯이 가입 신청일 기준 2-3일 정도 소요되나, 3일이 지났는데도 아무 연락이 없을 시 고객센터로 연락바랍니다.", true);
        questionService.createQuestion("회원 정보를 수정하고 싶은데 어떻게 해야 하나요?", "회원 정보 수정은 마이페이지에서 직접 수정하실 수 있습니다. 만약 어려운 점이 있으시면 고객센터로 연락 주시면 친절하게 안내해 드리겠습니다.", true);
        questionService.createQuestion("비밀번호를 잊어버렸어요. 어떻게 해야 하나요?", "비밀번호를 잊어버리셨을 경우, 로그인 페이지에서 '비밀번호 찾기' 기능을 이용하여 재설정할 수 있습니다. 이메일로 안내가 전송됩니다.", true);
        questionService.createQuestion("고객센터 연락처는 어떻게 되나요?", "고객센터 연락처는 000-0000-0000 입니다. 운영 시간은 평일 오전 9시부터 오후 6시까지이며, 토요일과 일요일은 휴무입니다.", true);
        questionService.createQuestion("재고 수량은 어떻게 확인할 수 있나요?", "재고 관리 페이지에서 실시간으로 재고 수량을 확인하실 수 있습니다. 또한, 재고 부족 시 자동으로 알림이 발송되니 주의하시기 바랍니다.", true);
        questionService.createQuestion("상품의 입고/출고 기록은 어디에서 확인할 수 있나요?", "재고 관리 페이지의 입고/출고 기록 탭에서 각 상품의 입고 및 출고 기록을 상세하게 확인하실 수 있습니다. 이를 통해 재고 이력을 관리하실 수 있습니다.", true);
        questionService.createQuestion("재고 품목의 추가/삭제는 어떻게 할 수 있나요?", "재고 관리 페이지에서 새로운 상품을 추가하거나 기존 상품을 삭제할 수 있습니다. 추가된 상품은 바로 재고에 반영되며, 삭제된 상품은 해당 창고 또는 매장에서 제거됩니다.", true);
        questionService.createQuestion("재고 이력을 확인할 수 있는 방법은 무엇인가요?", "재고 관리 페이지에서 각 상품의 입고 및 출고 이력을 확인하실 수 있습니다. 이를 통해 상품의 이동 경로 및 이력을 추적하실 수 있습니다.", true);
        questionService.createQuestion("여러 창고 또는 매장을 관리할 수 있나요?", "추후 업데이트 예정입니다.", true);
    }
}