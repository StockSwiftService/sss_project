package org.example.stockswiftservice.domain.purchase.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.domain.company.entity.Company;
import org.example.stockswiftservice.domain.stock.entity.Stock;
import org.example.stockswiftservice.global.baseentity.BaseEntity;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Purchase extends BaseEntity {
    private LocalDate purchaseDate;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    private Boolean deliveryStatus;
    private String significant;
    private Long allPrice;
    private Boolean approval;
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PurchaseStock> purchaseStocks;
    private String companyCode;
}
