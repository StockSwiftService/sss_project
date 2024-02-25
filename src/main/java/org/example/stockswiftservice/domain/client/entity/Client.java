package org.example.stockswiftservice.domain.client.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.example.stockswiftservice.domain.stock.entity.Stock;
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
    private String detailAddress;

    @OneToMany(mappedBy = "client")
    @JsonManagedReference
    private List<Stock> stocks;
}
