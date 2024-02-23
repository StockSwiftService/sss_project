package org.example.stockswiftservice.domain.client.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
    public static class ClientsSearchResponse {
        private final Page<Client> clients;
        private final List<Client> clientList;
    }

    @GetMapping("")
    public RsData<ClientsSearchResponse> clients(@RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Client> clients = this.clientService.getSearchList(kw, page);
        List<Client> clientList = this.clientService.getList();

        return RsData.of("S-1", "성공", new ClientsSearchResponse(clients,clientList));
    }


    @Getter
    @AllArgsConstructor
    public static class ClientResponse {
        private final Client client;
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
        @NotBlank
        private String clientNameModify;
        @NotBlank
        private String repNameModify;
        @NotBlank
        private String phoneNumberModify;
        @NotBlank
        private String addressModify;
        @NotBlank
        private String detailAddressModify;
    }

    @PatchMapping("/{id}")
    public RsData<Client> modify(@Valid @RequestBody ModifyRequest modifyRequest, @PathVariable("id") Long id) {
        Client client = this.clientService.getClient(id);

        RsData<Client> modifyClient = clientService.modify(client, modifyRequest.getClientNameModify(), modifyRequest.getRepNameModify(),
                modifyRequest.getPhoneNumberModify(), modifyRequest.getAddressModify(), modifyRequest.getDetailAddressModify());

        return modifyClient;
    }

    @DeleteMapping("/{id}")
    public RsData<Client> delete(@PathVariable("id") Long id) {
        Client client = this.clientService.getClient(id);
        RsData<Client> clientRsData = this.clientService.delete(client);
        return clientRsData;
    }

    @PostMapping("/deleteMultiple")
    public RsData<List<Client>> deleteMultiple(@RequestBody List<Long> id) {
        List<Client> deletedClients = clientService.deleteMultiple(id);
        return RsData.of("S-5", "정보 삭제", deletedClients);
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
