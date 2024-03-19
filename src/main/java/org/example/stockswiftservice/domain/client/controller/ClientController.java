package org.example.stockswiftservice.domain.client.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.client.entity.Client;
import org.example.stockswiftservice.domain.client.service.ClientService;
import org.example.stockswiftservice.domain.company.entity.Company;
import org.example.stockswiftservice.domain.member.service.MemberService;
import org.example.stockswiftservice.global.jwt.JwtProvider;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.example.stockswiftservice.domain.global.filter.JwtAuthorizationFilter.extractAccessToken;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://frontapp-oj4tpflt4-jhseos-projects.vercel.app")
public class ClientController {
    private final ClientService clientService;
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Getter
    @AllArgsConstructor
    public static class ClientsSearchResponse {
        private final Page<Client> clients;
        private final List<Client> clientList;
    }

    @GetMapping("")
    public RsData<ClientsSearchResponse> clients(@RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(value = "page", defaultValue = "0") int page, HttpServletRequest request) {
        String token = extractAccessToken(request); //헤더에 담긴 쿠키에서 토큰 요청
        Long userId = ((Integer) jwtProvider.getClaims(token).get("id")).longValue(); //유저의 아이디 값
        Company company = this.memberService.findbyId(userId).get().getCompany();
        String companyCode = company.getCompanyCode();

        Page<Client> clients = this.clientService.getSearchList(kw, page, companyCode);
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
    public RsData<CreateResponse> create (@Valid @RequestBody CreateRequest createRequest, HttpServletRequest request) {
        String token = extractAccessToken(request); //헤더에 담긴 쿠키에서 토큰 요청
        Long userId = ((Integer) jwtProvider.getClaims(token).get("id")).longValue(); //유저의 아이디 값
        Company company = this.memberService.findbyId(userId).get().getCompany();
        String companyCode = company.getCompanyCode();

        RsData<Client> client = clientService.create(companyCode, createRequest.getClientName(), createRequest.getRepName(),
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
        return RsData.of("S-5", "정보 삭제", null);
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
    public RsData<NameResponse> checkItemName(@Valid @RequestBody NameRequest nameRequest) {
        Optional<Client> clientName = clientService.findByClientName(nameRequest.getClientName());
        if (clientName.isPresent()) {
            return RsData.of("S-6", "중복된 거래처명", new NameResponse(clientName));
        } else {
            return RsData.of("S-7", "중복되지 않은 거래처명", null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Client>> searchClients(@RequestParam(value = "clientName") String clientName) {
        List<Client> clients;
        if (clientName != null && !clientName.isEmpty()) {
            // 검색어가 있을 경우, 해당 검색어를 포함하는 거래처 목록을 조회
            clients = clientService.findByClientNameContaining(clientName);
        } else {
            // 검색어가 없을 경우, 모든 거래처 목록을 조회
            clients = clientService.getList();
        }
        return ResponseEntity.ok(clients);
    }

//    @GetMapping("/search")
//    public RsData<ClientController.ClientsResponse> searchClients(@RequestParam("clientName") String searchText) {
//        List<Client> clients = clientService.searchByName(searchText);
//        return RsData.of("S-1", "검색 성공", new ClientController.ClientsResponse(clients));
//    }
}
