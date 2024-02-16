package org.example.stockswiftservice.domain.salemanagement.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.salemanagement.entity.SalesManagemant;
import org.example.stockswiftservice.domain.salemanagement.service.SalesManagementService;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.MimeTypeUtils.ALL_VALUE;

@RestController
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class SalesManagementController {
    private final SalesManagementService saleManagementService;

    @Data
    public static class CreateSalesManagement {
        private Purchase purchase;
    }

    @AllArgsConstructor
    @Getter
    public static class CreateSM {
        private final SalesManagemant salesManagemant;
    }

    @PostMapping(value = "/sales", consumes = ALL_VALUE)
    public RsData<CreateSM> CreateSalesM(@RequestBody CreateSalesManagement createSalesManagement){
       SalesManagemant salesManagemant = saleManagementService.getTrueParchaseTotal(createSalesManagement.getPurchase());

        return RsData.of("R-1", "성공", new CreateSM(salesManagemant));
    }
}