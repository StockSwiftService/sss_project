package org.example.stockswiftservice.domain.purchase.service;

import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.domain.purchase.repository.PurchaseRepository;
import org.example.stockswiftservice.domain.stock.entity.Stock;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;

    public List<Purchase> getList() {
        return this.purchaseRepository.findAll();
    }

    public RsData<Purchase> create(LocalDate purchaseDate, Client client, Boolean deliveryStatus, String significant, List<Stock> stocks, Long allPrice) {
        Purchase purchase = Purchase.builder()
                .purchaseDate(purchaseDate)
                .client(client)
                .deliveryStatus(deliveryStatus)
                .significant(significant)
                .stocks(stocks)
                .allPrice(allPrice)
                .build();

        purchaseRepository.save(purchase);

        return RsData.of("1", "회원가입 완료", purchase);
    }


//    public RsData<SiteUser> signup(String username, String nickName, String password, String email, String name, LocalDate birthDate) {
//        SiteUser siteUser = SiteUser.builder()
//                .username(username)
//                .nickName(nickName)
//                .password(passwordEncoder.encode(password))
//                .email(email)
//                .name(name)
//                .birthDate(birthDate)
//                .build();
//
//        userRepository.save(siteUser);
//
//        return RsData.of("1", "회원가입 완료", siteUser);
//    }
}
