package org.example.stockswiftservice.domain.stock.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.global.baseentity.BaseEntity;

import java.security.PublicKey;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock extends BaseEntity {
    private String clientName;
    private String itemName;
    private Long quantity;
    private Long purchasePrice;
    private Long salesPrice;
    @ManyToMany
    private List<Purchase> purchases;
}
