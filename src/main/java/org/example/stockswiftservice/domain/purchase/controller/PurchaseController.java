package org.example.stockswiftservice.domain.purchase.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.service.PurchaseService;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.MimeTypeUtils.ALL_VALUE;

@RestController
@RequestMapping(value = "/api/v1/purchase", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Data
    public static class ApprovalPurchase {
        private Long purchaseTotal;
        private String purchaseDate;
    }

    @AllArgsConstructor
    @Getter
    public static class ApprovalPur {
        private final Purchase purchase;
    }

    @PostMapping(value = "/approval", consumes = ALL_VALUE)
    public RsData<ApprovalPur> create(@Valid @RequestBody ApprovalPurchase createPurchase ){

        Purchase purchases = purchaseService.create(createPurchase.getPurchaseTotal(), createPurchase.getPurchaseDate());

        return RsData.of("R-1", "성공", new ApprovalPur(purchases));
    }


    @Data
    public static class GetPurchaseDate {
        private String date;
    }

    @AllArgsConstructor
    @Getter
    public static class PurchaseList {
        private final List<Purchase> purchase;
    }

    @PostMapping(value = "/list", consumes = ALL_VALUE)
    public RsData<PurchaseList> getList(@RequestBody GetPurchaseDate getPurchaseDate){

        List<Purchase> purchases = purchaseService.getPurchaseList(getPurchaseDate.getDate());

        return RsData.of("R-1", "성공", new PurchaseList(purchases));
    }
}