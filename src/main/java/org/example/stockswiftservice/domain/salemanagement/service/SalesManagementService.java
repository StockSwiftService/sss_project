package org.example.stockswiftservice.domain.salemanagement.service;

import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseRepository;
import org.example.stockswiftservice.domain.salemanagement.entity.SalesManagemant;
import org.example.stockswiftservice.domain.salemanagement.repository.SalesManagementRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SalesManagementService {
    private final SalesManagementRepository saleManagementRepository;
    private final PurchaseRepository purchaseRepository;
// 디테일에서는 해당 데이터가 true를 가졌을 경우, 같은 날짜일 경우 해당 데이터들을 살펴볼 수 있다. 그리고 해당 데이터들의 합계도 나온다.
// 판매 관리 달력에는 데이터들의 합계만 표시가 된다.
// 날짜를 비교해서 같은 날짜끼리 ArrayList배열에 담고 총액을 더한다.

    //해당 날짜의 승인 요청된 구매리스트
    public List<Purchase> getTrueParchase() {
        List<Purchase> lists = purchaseRepository.findAll();

        return lists;
    }

    //해당 날짜의 승인 요청된 구매리스트합산, 승인이 요청되면 controller로 해당 purchase가 전송되고 여기서 핸들링작업
    public SalesManagemant getTrueParchaseTotal(Purchase purchase) {
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        String purchaseDate = String.valueOf(purchase.getPurchaseDate());

        Optional<SalesManagemant> optionalSalesManagement = saleManagementRepository.findBySalesDate(purchaseDate);

        HashMap<String, Long> datePurchaseTotal = new HashMap();

        datePurchaseTotal.put(purchaseDate, purchase.getPurchaseTotal());

        SalesManagemant salesManagement;
        if (optionalSalesManagement.isPresent()) {
            salesManagement = optionalSalesManagement.get();
            Long updatedTotalSales = salesManagement.getTotalSales() + datePurchaseTotal.get(purchaseDate);
            salesManagement.setTotalSales(updatedTotalSales);
        } else {
            salesManagement = SalesManagemant.builder()
                    .salesDate(purchaseDate)
                    .totalSales(datePurchaseTotal.get(purchaseDate))
                    .salesNumber(1L)
                    .build();
        }
        SalesManagemant salesManagemantData = saleManagementRepository.save(salesManagement);

        return salesManagemantData;
    }

    //해당 날짜의 승인 요청된 판매리스트
    public void getTrueSale() {

    }

    //해당 날짜의 승인 요청된 판매리스트합산
    public void getTrueSaleTotal() {

    }

    // 판매리스트와 구매리스트의 합산
    public void totalAmount() {

    }

}
