package org.example.stockswiftservice.domain.client.repository;

import org.example.stockswiftservice.domain.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query(value = "SELECT * FROM client WHERE client_name LIKE %?1% OR rep_name LIKE %?1% OR phone_number LIKE %?1%", nativeQuery = true)
    List<Client> findByClientNameOrRepNameOrPhoneNumberContaining(String searchText);
}
