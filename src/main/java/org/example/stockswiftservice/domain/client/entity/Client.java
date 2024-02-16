package org.example.stockswiftservice.domain.client.entity;

import jakarta.persistence.Entity;
import lombok.*;
import org.example.stockswiftservice.global.baseentity.BaseEntity;

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
}
