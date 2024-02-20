package org.example.stockswiftservice.domain.purchase.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.service.PurchaseService;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @AllArgsConstructor
    @Getter
    public static class PurchasesResponse {
        private final List<Purchase> purchases;
    }

    @GetMapping("")
    public RsData<PurchasesResponse> purchases() {
        List<Purchase> purchases = this.purchaseService.getList();

        return RsData.of("S-1", "성공", new PurchasesResponse(purchases));
    }
}
