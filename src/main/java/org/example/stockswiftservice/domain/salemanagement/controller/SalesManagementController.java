package org.example.stockswiftservice.domain.salemanagement.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.salemanagement.entity.SalesManagement;
import org.example.stockswiftservice.domain.salemanagement.service.SalesManagementService;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class SalesManagementController {
    private final SalesManagementService saleManagementService;

    @Data
    public static class CreateSalesManagement {
//        private Purchase purchase;
        private Long purchaseId;
    }

    @AllArgsConstructor
    @Getter
    public static class CreateSM {
        private final SalesManagement salesManagement;
    }

    // 승인 요청을 받았을 경우
    @PostMapping(value = "/sales", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RsData<CreateSM> CreateSalesM(@RequestBody CreateSalesManagement createSalesManagement){
        SalesManagement salesManagement = saleManagementService.printTotalSales(createSalesManagement.getPurchaseId());
        return RsData.of("R-1", "성공", new CreateSM(salesManagement));
    }

}