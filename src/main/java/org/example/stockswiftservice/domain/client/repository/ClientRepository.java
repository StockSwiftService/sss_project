package org.example.stockswiftservice.domain.client.repository;

import org.example.stockswiftservice.domain.client.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByClientName(String clientName);

    Page<Client> findAll(Pageable pageable);
    Page<Client> findAll(Specification<Client> spec, Pageable pageable);
}
