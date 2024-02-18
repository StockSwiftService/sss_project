package org.example.stockswiftservice.domain.client.service;

import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.domain.client.repository.ClientRepository;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public List<Client> getList() {
        return this.clientRepository.findAll();
    }

    public Client getClient(Long id) {
        Optional<Client> optionalClient = this.clientRepository.findById(id);

        if (optionalClient.isEmpty()) {
            throw  new RuntimeException("없음");
        }

        return optionalClient.get();
    }

    public RsData<Client> create(String clientName, String repName, String phoneNumber, String address){
        Client client = Client.builder()
                .clientName(clientName)
                .repName(repName)
                .phoneNumber(phoneNumber)
                .address(address)
                .build();

        clientRepository.save(client);

        return RsData.of("S-3", "거래처 생성 성공", client);
    }

    public RsData<Client> modify(Client client, String clientName, String repName, String phoneNumber, String address) {
        client.setClientName(clientName);
        client.setRepName(repName);
        client.setPhoneNumber(phoneNumber);
        client.setAddress(address);

        clientRepository.save(client);

        return RsData.of("S-4", "정보 수정", client);
    }

    public RsData<Client> delete(Client client) {
        this.clientRepository.delete(client);
        return RsData.of("S-5", "정보 삭제", client);
    }
}
