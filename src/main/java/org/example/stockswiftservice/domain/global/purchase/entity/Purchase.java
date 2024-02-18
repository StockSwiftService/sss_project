package org.example.stockswiftservice.domain.global.purchase.entity;

import jakarta.persistence.Entity;
import org.example.stockswiftservice.domain.global.baseentity.BaseEntity;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties;

import java.time.LocalDate;

@Entity
public class Purchase extends BaseEntity {
    private LocalDate purchaseDate;
    private boolean deliveryStatus;
    private String significant;
}
