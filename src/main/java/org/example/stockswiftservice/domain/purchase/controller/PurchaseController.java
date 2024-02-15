package org.example.stockswiftservice.domain.purchase.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.service.PurchaseService;
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
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Data
    public static class CreatePurchase {
        private Long purchaseTotal;
        private String purchaseDate;
    }

    @AllArgsConstructor
    @Getter
    public static class CreatePur {
        private final Purchase purchase;
    }

    @PostMapping(value = "/create", consumes = ALL_VALUE)
    public RsData<CreatePur> create(@Valid @RequestBody CreatePurchase createPurchase ){

        Purchase purchases = purchaseService.create(createPurchase.getPurchaseTotal(), createPurchase.getPurchaseDate());

        return RsData.of("R-1", "성공", new CreatePur(purchases));
    }
}
