package org.example.stockswiftservice.domain.purchase.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.example.stockswiftservice.global.baseentity.BaseEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseStock extends BaseEntity {
    private String itemName;
    private Long inputQuantity;
    @ManyToOne
    private Purchase purchase;
}
