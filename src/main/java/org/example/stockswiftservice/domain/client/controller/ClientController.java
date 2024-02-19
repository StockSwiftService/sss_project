package org.example.stockswiftservice.domain.client.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.domain.client.service.ClientService;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @Getter
    @AllArgsConstructor
    public static class ClientsResponse {
        private final Page<Client> clients;
    }

    @GetMapping("")
    public RsData<ClientsResponse> clients(@RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Client> clients = this.clientService.getList(kw, page);

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
        private String clientName;
        @NotBlank
        private String repName;
        @NotBlank
        private String phoneNumber;
        @NotBlank
        private String address;
        @NotBlank
        private String detailAddress;
    }

    @Getter
    @AllArgsConstructor
    public static class CreateResponse {
        private final Client client;
    }

    @PostMapping("")
    public RsData<CreateResponse> create (@Valid @RequestBody CreateRequest createRequest) {
        RsData<Client> client = clientService.create(createRequest.getClientName(), createRequest.getRepName(),
                createRequest.getPhoneNumber(), createRequest.getAddress(), createRequest.getDetailAddress());
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
        private String clientName;
        private String repName;
        private String phoneNumber;
        private String address;
        private String detailAddress;
    }

    @PatchMapping("/{id}")
    public RsData<Client> modify(@Valid ModifyRequest modifyRequest, @PathVariable("id") Long id) {
        Client client = this.clientService.getClient(id);

        RsData<Client> modifyClient = clientService.modify(client, modifyRequest.getClientName(), modifyRequest.getRepName(),
                modifyRequest.getPhoneNumber(), modifyRequest.getAddress(), modifyRequest.getDetailAddress());

        return modifyClient;
    }

    @DeleteMapping("/{id}")
    public RsData<Client> delete(@PathVariable("id") Long id) {
        Client client = this.clientService.getClient(id);
        RsData<Client> clientRsData = this.clientService.delete(client);
        return clientRsData;
    }
    @AllArgsConstructor
    @Getter
    public static class NameResponse {
        private final Optional<Client> client;
    }

    @Data
    public static class NameRequest {
        @NotBlank
        private String clientName;
    }

    @PostMapping("/check")
    public RsData<NameResponse> checkClientName(@Valid @RequestBody NameRequest nameRequest) {
        Optional<Client> clientName = clientService.findByClientName(nameRequest.getClientName());
        if (clientName.isPresent()) {
            return RsData.of("S-6", "중복된 거래처명", new NameResponse(clientName));
        } else {
            return RsData.of("S-7", "사용 가능", null);
        }
    }

}
