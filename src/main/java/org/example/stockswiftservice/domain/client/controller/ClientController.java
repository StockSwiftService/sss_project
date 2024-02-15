package org.example.stockswiftservice.domain.client.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.domain.client.service.ClientService;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
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

    @PostMapping("")
    public RsData<CreateResponse> create (@Valid @RequestBody CreateRequest createRequest) {
        RsData<Client> client = clientService.create(createRequest.getClientCode(), createRequest.getClientName(),
                createRequest.getRepName(), createRequest.getPhoneNumber(), createRequest.getMobileNumber(), createRequest.getAddress());
        if (client.isFail()) return (RsData) client;

        return RsData.of(client.getResultCode(), client.getMsg(), new CreateResponse(client.getData()));
    }

    @AllArgsConstructor
    @Getter
    public static class ModifyResponse {
        private final Client client;
    }

    @Data
    public static class ModifyRequest {
        private String clientCode;
        private String clientName;
        private String repName;
        private String phoneNumber;
        private String mobileNumber;
        private String address;
    }

    @PatchMapping("/{id}")
    public RsData<Client> modify(@Valid @RequestBody ModifyRequest modifyRequest, @PathVariable("id") Long id) {
        Client client = this.clientService.getClient(id);

        RsData<Client> modifyClient = clientService.modify(client, modifyRequest.getClientCode(), modifyRequest.getClientName(), modifyRequest.getRepName(), modifyRequest.getPhoneNumber(), modifyRequest.getMobileNumber(), modifyRequest.getAddress());

        return modifyClient;
    }

    @DeleteMapping("/{id}")
    public RsData<Client> delete(@PathVariable("id") Long id) {
        Client client = this.clientService.getClient(id);
        RsData<Client> clientRsData = this.clientService.delete(client);
        return clientRsData;
    }
}
