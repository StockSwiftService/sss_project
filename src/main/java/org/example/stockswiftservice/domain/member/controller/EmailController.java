package org.example.stockswiftservice.domain.member.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
//import org.example.stockswiftservice.domain.member.service.EmailService;
import org.example.stockswiftservice.domain.member.service.EmailService;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.MimeTypeUtils.ALL_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/email", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://frontapp-oj4tpflt4-jhseos-projects.vercel.app")
public class EmailController {
    private final EmailService emailService;

    @AllArgsConstructor
    @Getter
    public static class EmailResponse {
        private final String number;
    }

    @Data
    public static class EmailRequest {
        @NotBlank
        @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$")
        private String email;
    }

    @PostMapping(value = "/send", consumes = ALL_VALUE)
    public RsData<EmailResponse> sendVerificationCode(@Valid @RequestBody EmailRequest emailRequest) {
        String email = emailRequest.getEmail();

        // 이메일 중복 검사
        if (emailService.isEmailDuplicate(email) == true) {
            return RsData.of("S-5", "중복된 이메일", null);
        } else {
            int number = emailService.sendEmail(emailRequest.getEmail());
            String num = "" + number;
            return RsData.of("S-6", "이메일 인증번호 발송완료", new EmailResponse(num));
        }
    }
}
