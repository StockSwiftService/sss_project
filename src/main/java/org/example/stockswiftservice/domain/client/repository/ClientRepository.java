package org.example.stockswiftservice.domain.client.repository;

import org.example.stockswiftservice.domain.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
