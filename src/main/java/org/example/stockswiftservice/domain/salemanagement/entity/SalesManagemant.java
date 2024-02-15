package org.example.stockswiftservice.domain.salemanagement.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.stockswiftservice.global.baseentity.BaseEntity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SalesManagemant extends BaseEntity {
    private String salesDate;

    private Long totalSales;

    private Long salesNumber;
}
