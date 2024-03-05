package org.example.stockswiftservice.domain.sale.entity;

import jakarta.persistence.Entity;
import lombok.*;
import org.example.stockswiftservice.global.baseentity.BaseEntity;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sale extends BaseEntity {

    private LocalDate saleDate;

    private Long allSalePrice;
}
