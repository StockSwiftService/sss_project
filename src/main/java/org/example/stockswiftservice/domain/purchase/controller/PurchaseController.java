package org.example.stockswiftservice.domain.purchase.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.entity.PurchaseStock;
import org.example.stockswiftservice.domain.purchase.service.PurchaseService;
import org.example.stockswiftservice.domain.salemanagement.controller.SalesManagementController;
import org.example.stockswiftservice.domain.salemanagement.entity.SalesManagement;
import org.example.stockswiftservice.domain.salemanagement.service.SalesManagementService;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final SalesManagementService salesManagementService;

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

    @Getter
    @AllArgsConstructor
    public static class PurchasesSearchResponse {
        private final Page<Purchase> purchases;
    }

    @Getter
    @AllArgsConstructor
    public static class PurchasesResponse {
        private final List<Purchase> purchases;
    }

    @GetMapping(value = "")
    public RsData<PurchasesSearchResponse> purchases(@RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "whether", defaultValue = "false") boolean whether) {
        Page<Purchase> purchases = this.purchaseService.getSearchList(kw, page, whether);

        return RsData.of("S-1", "성공", new PurchasesSearchResponse(purchases));
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

    @PostMapping(value = "/list")
    public RsData<PurchaseList> getList(@RequestBody GetPurchaseDate getPurchaseDate){

        List<Purchase> purchases = purchaseService.getPurchaseList(getPurchaseDate.getDate());

        return RsData.of("R-1", "성공", new PurchaseList(purchases));
    }

    @Data
    public static class purchaseRequest {
        private LocalDate purchaseDate;
        private Client selectedClient;
        private Boolean deliveryStatus;
        private String significant;
        private List<PurchaseStock> filteredItems;
        private Long allPrice;
    }

    @PostMapping(value = "/create")
    public RsData<Purchase> create(@Valid @RequestBody purchaseRequest purchaseRequest) {

        RsData<Purchase> rsData = this.purchaseService.create(purchaseRequest.getPurchaseDate(), purchaseRequest.getSelectedClient(), purchaseRequest.getDeliveryStatus(), purchaseRequest.getSignificant(), purchaseRequest.getFilteredItems(), purchaseRequest.getAllPrice());

        return rsData;
    }

    @Data
    public static class ApprovalRequest {
        private List<Long> ids;
    }

    @PostMapping(value = "/approvalRequest")
    public RsData<SalesManagementController.CreateSM> approval(@Valid @RequestBody ApprovalRequest approvalRequest) {
        this.purchaseService.approval(approvalRequest.getIds());

        List<SalesManagement> salesManagementList = new ArrayList<>();
        for (Long num : approvalRequest.getIds()) {
            List<SalesManagement> salesManagement = salesManagementService.printTotalSales(num);
            salesManagementList.addAll(salesManagement);
        }

        return RsData.of("R-1", "성공", new SalesManagementController.CreateSM(salesManagementList));
    }

    @PostMapping(value = "/approvalCancelRequest")
    public RsData<PurchasesResponse> approvalCancel(@Valid @RequestBody ApprovalRequest approvalRequest) {
        List<Purchase> purchases = this.purchaseService.approvalCancel(approvalRequest.getIds());

        return RsData.of("S-3", "승인 취소 성공", new PurchasesResponse(purchases));
    }

    @PostMapping(value = "/delete")
    public RsData<PurchasesResponse> delete(@Valid @RequestBody ApprovalRequest approvalRequest) {
        List<Purchase> purchases = this.purchaseService.delete(approvalRequest.getIds());

        return RsData.of("S-4", "삭제 성공", new PurchasesResponse(purchases));
    }
}
