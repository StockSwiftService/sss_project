package org.example.stockswiftservice.domain.client.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.stockswiftservice.domain.purchase.entity.Purchase;
import org.example.stockswiftservice.global.baseentity.BaseEntity;

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
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Purchase> purchases;
}
