package org.example.stockswiftservice.domain.client.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.domain.client.service.ClientService;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @Getter
    @AllArgsConstructor
    public static class ClientsResponse {
        private final List<Client> clients;
    }

    @GetMapping("")
    public RsData<ClientsResponse> clients() {
        List<Client> clients = this.clientService.getList();

        return RsData.of("S-1", "성공", new ClientsResponse(clients));
    }

    @Getter
    @AllArgsConstructor
    public static class ClientResponse {
        private final Client clients;
    }

    @GetMapping("/{id}")
    public RsData<ClientResponse> client(@PathVariable("id") Long id) {
        Client client = this.clientService.getClient(id);

        return RsData.of("S-2", "성공", new ClientResponse(client));
    }

    @Data
    public static class CreateRequest {
        @NotBlank
        private String clientCode;
        @NotBlank
        private String clientName;
        @NotBlank
        private String repName;
        @NotBlank
        private String phoneNumber;
        @NotBlank
        private String mobileNumber;
        @NotBlank
        private String address;
    }

    @Getter
    @AllArgsConstructor
    public static class CreateResponse {
        private final Client client;
    }
}
