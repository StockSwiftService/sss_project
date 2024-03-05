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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/purchase", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
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

    @Data
    public static class GetPurchaseDate {
        private String date;
    }

    @AllArgsConstructor
    @Getter
    public static class PurchaseList {
        private final List<Purchase> purchase;
    }

    @AllArgsConstructor
    @Getter
    public static class PurchasesResponse {
        private final List<Purchase> purchases;
    }

    @GetMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RsData<PurchasesResponse> purchases() {
        List<Purchase> purchases = this.purchaseService.getList();

        return RsData.of("S-1", "성공", new PurchasesResponse(purchases));
    }

    @GetMapping(value = "/approval", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RsData<PurchasesResponse> approvalPurchases() {
        List<Purchase> purchases = this.purchaseService.getApprovalList();

        return RsData.of("S-1", "성공", new PurchasesResponse(purchases));
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

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RsData<Purchase> signup(@Valid @RequestBody purchaseRequest purchaseRequest) {

        RsData<Purchase> rsData = this.purchaseService.create(purchaseRequest.getPurchaseDate(), purchaseRequest.getSelectedClient(), purchaseRequest.getDeliveryStatus(), purchaseRequest.getSignificant(), purchaseRequest.getFilteredItems(), purchaseRequest.getAllPrice());

        return rsData;
    }

    @Data
    public static class ApprovalRequest {
        private List<Long> ids;
    }

    @PostMapping(value = "/approvalRequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RsData<SalesManagementController.CreateSM> approval(@Valid @RequestBody ApprovalRequest approvalRequest) {
        List<SalesManagement> salesManagementList = new ArrayList<>();
        for (Long num : approvalRequest.getIds()) {
            List<SalesManagement> salesManagement = salesManagementService.printTotalSales(num);
            salesManagementList.addAll(salesManagement);
        }
        this.purchaseService.approval(approvalRequest.getIds());

        return RsData.of("R-1", "성공", new SalesManagementController.CreateSM(salesManagementList));
    }

    @PostMapping(value = "/approvalCancelRequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void approvalCancel(@Valid @RequestBody ApprovalRequest approvalRequest) {
        this.purchaseService.approvalCancel(approvalRequest.getIds());
    }

    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@Valid @RequestBody ApprovalRequest approvalRequest) {
        this.purchaseService.delete(approvalRequest.getIds());
    }
}
