package org.example.stockswiftservice.domain.client.repository;

import org.example.stockswiftservice.domain.client.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByClientName(String clientName);

    Page<Client> findAll(Pageable pageable);
    Page<Client> findAll(Specification<Client> spec, Pageable pageable);

    List<Client> findByClientNameContaining(String clientName);
    @Query(value = "SELECT * FROM client WHERE client_name LIKE %?1% OR rep_name LIKE %?1% OR phone_number LIKE %?1%", nativeQuery = true)
    List<Client> findByClientNameOrRepNameOrPhoneNumberContaining(String searchText);
}


