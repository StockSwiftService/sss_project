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
    private String itemCode;
    private String itemName;
    private String transactionDate;
    private String clientName;
    private Long quantity;
    private Long unitPrice;
    private Long totalAmount;
}
