package org.example.stockswiftservice.domain.purchase.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.global.baseentity.BaseEntity;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Purchase extends BaseEntity {
    private LocalDate purchaseDate;
    private boolean deliveryStatus;
    private String significant;
    @ManyToOne
    private Client client;
}
