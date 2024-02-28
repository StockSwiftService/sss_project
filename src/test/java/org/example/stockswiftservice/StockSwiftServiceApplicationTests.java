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
    void clientTest() {
        clientService.create("이니시스", "이지훈", "01031245742", "대전 중구 계룡로816번길 32-4", "105호");
        clientService.create("다음카카오", "김범수", "01045678901", "제주특별자치도 제주시 첨단로 242", "105호");
        clientService.create("네이버", "이해진", "01098765432", "경기도 성남시 분당구 불정로 6", "105호");
        clientService.create("삼성전자", "김기남", "01012345678", "서울특별시 서초구 서초대로 74길 11", "105호");
        clientService.create("LG전자", "권봉석", "01087654321", "서울특별시 영등포구 여의대로 128", "105호");
        clientService.create("SK하이닉스", "이석희", "01056789012", "경기도 이천시 부발읍 경충대로 2091", "105호");
        clientService.create("현대자동차", "정의선", "01034567890", "서울특별시 서초구 헌릉로 12", "105호");
        clientService.create("기아자동차", "송호성", "01023456789", "서울특별시 서초구 양재대로 12길 37", "105호");
        clientService.create("포스코", "최정우", "01078901234", "경상북도 포항시 남구 포스코대로 1", "105호");
        clientService.create("롯데케미칼", "김교현", "01089012345", "서울특별시 송파구 올림픽로 300", "105호");
        clientService.create("한화솔루션", "김동관", "01090123456", "서울특별시 중구 청계천로 1", "105호");
        clientService.create("CJ제일제당", "신현재", "01067890123", "서울특별시 중구 을지로 100", "105호");
        clientService.create("두산중공업", "박지원", "01078901234", "경상남도 창원시 성산구 두산볼보로 22", "105호");
        clientService.create("LG화학", "신학철", "01089012345", "서울특별시 영등포구 여의나루로 4", "105호");
        clientService.create("셀트리온", "서정진", "01090123456", "인천광역시 연수구 송도과학로 85", "105호");
        clientService.create("넥슨", "김정주", "01012345678", "경기도 성남시 분당구 판교로 256번길 7", "105호");
        clientService.create("NC소프트", "김택진", "01023456789", "경기도 성남시 분당구 판교로 256번길 12", "105호");
        clientService.create("넷마블", "방준혁", "01034567890", "서울특별시 구로구 디지털로 300", "105호");
        clientService.create("카카오", "여민수", "01045678901", "제주특별자치도 제주시 첨단로 242", "105호");
        clientService.create("우아한형제들", "김봉진", "01056789012", "서울특별시 송파구 올림픽로 300", "105호");
        clientService.create("야놀자", "이수진", "01067890123", "서울특별시 중구 퇴계로 123", "105호");
    }

    @Test
    void stockTest() {
        stockService.create("야놀자", "", "", "", "")
    }
}