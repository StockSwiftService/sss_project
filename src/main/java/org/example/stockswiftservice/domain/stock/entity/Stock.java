package org.example.stockswiftservice.domain.stock.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.global.baseentity.BaseEntity;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference
    private Client client;
    private String itemName;
    private Long defaultQuantity;
    private Long quantity;
    private Long purchasePrice;
    private Long salesPrice;

    @CreatedDate
    private LocalDateTime createDate;
}
