package org.example.stockswiftservice.domain.client.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.*;
import lombok.*;
import org.example.stockswiftservice.domain.stock.entity.Stock;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.global.baseentity.BaseEntity;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client extends BaseEntity {
    private String clientName;
    private String repName;
    private String phoneNumber;
    private String address;
    private String detailAddress;

    @CreatedDate
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "client")
    @JsonManagedReference
    private List<Stock> stocks;
}
