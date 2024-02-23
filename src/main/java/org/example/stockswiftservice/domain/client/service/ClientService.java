package org.example.stockswiftservice.domain.client.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.domain.client.repository.ClientRepository;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public List<Client> getList() {
        return this.clientRepository.findAll();
    }

    public Page<Client> getSearchList(String kw, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 6, Sort.by(sorts));
        Specification<Client> spec = search(kw);
        return this.clientRepository.findAll(spec, pageable);
    }

    public Client getClient(Long id) {
        Optional<Client> optionalClient = this.clientRepository.findById(id);

        if (optionalClient.isEmpty()) {
            throw  new RuntimeException("없음");
        }

        return optionalClient.get();
    }

    public RsData<Client> create(String clientName, String repName, String phoneNumber, String address, String detailAddress){
        Client client = Client.builder()
                .clientName(clientName)
                .repName(repName)
                .phoneNumber(phoneNumber)
                .address(address)
                .detailAddress(detailAddress)
                .build();

        clientRepository.save(client);

        return RsData.of("S-3", "거래처 생성 성공", client);
    }

    public RsData<Client> modify(Client client, String clientName, String repName, String phoneNumber, String address, String detailAddress) {
        client.setClientName(clientName);
        client.setRepName(repName);
        client.setPhoneNumber(phoneNumber);
        client.setAddress(address);
        client.setDetailAddress(detailAddress);

        clientRepository.save(client);

        return RsData.of("S-4", "정보 수정", client);
    }

    public RsData<Client> delete(Client client) {
        this.clientRepository.delete(client);
        return RsData.of("S-5", "정보 삭제", client);
    }

    public List<Client> deleteMultiple(List<Long> id) {
        List<Client> clientsToDelete = clientRepository.findAllById(id);
        clientRepository.deleteAll(clientsToDelete);
        return clientsToDelete;
    }

    public Optional<Client> findByClientName(String clientName) {
        return clientRepository.findByClientName(clientName);
    }

    private Specification<Client> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Client> a, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                return cb.or(
                        cb.like(a.get("clientName"), "%" + kw + "%"),
                        cb.like(a.get("repName"), "%" + kw + "%"),
                        cb.like(a.get("phoneNumber"), "%" + kw + "%"),
                        cb.like(a.get("address"), "%" + kw + "%"),
                        cb.like(a.get("detailAddress"), "%" + kw + "%")
                );
            }
        };
    }
}
