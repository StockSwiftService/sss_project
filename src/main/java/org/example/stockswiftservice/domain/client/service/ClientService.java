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

    public RsData<Client> create(String clientCode, String clientName, String repName, String phoneNumber, String mobileNumber, String address){
        Client client = Client.builder()
                .clientCode(clientCode)
                .clientName(clientName)
                .repName(repName)
                .phoneNumber(phoneNumber)
                .mobileNumber(mobileNumber)
                .address(address)
                .build();

        clientRepository.save(client);

        return RsData.of("S-3", "거래처 생성 성공", client);
    }
}
