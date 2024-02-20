package org.example.stockswiftservice.domain.stock.entity;

import jakarta.persistence.Entity;
import lombok.*;
import org.example.stockswiftservice.global.baseentity.BaseEntity;

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
}