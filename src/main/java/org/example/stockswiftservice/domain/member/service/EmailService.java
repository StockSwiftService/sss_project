package org.example.stockswiftservice.domain.member.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.company.entity.Company;
import org.example.stockswiftservice.domain.company.repository.CompanyRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final CompanyRepository companyRepository;
    private final JavaMailSender mailSender;
    private static final String senderEmail = "joeun065@gmail.com";
    private static int number;
    // 랜덤 발송 번호를 보내기 위한 구문
    public static void createNumber(){
        //(int) Math.random() * (최댓값-최소값+1) + 최소값
        number = (int)(Math.random() * (90000)) + 10000;
    }
    // 이메일 인증을 하기 위한 createEmail 구문
    public MimeMessage createEmail(String email){
        createNumber();
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("Stock Swift Service 이메일 인증");
            String body = "";
            body += "<h1>" + "안녕하세요 Stock Swift Service 입니다." + "<h1>";
            body += "<h2>" + "아래 인증코드를 입력해 주세요." + "</h2>";
            body += "<h1>" + number + "</h1>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e){
            throw new RuntimeException(e);
        }
        return message;
    }
    public boolean isEmailDuplicate(String email) {
        Optional<Company> existingEmail =  companyRepository.findByEmail(email);
        return existingEmail.isPresent(); // true면 중복, false면 중복 아님
    }
    public int sendEmail(String email){
        MimeMessage message = createEmail(email);
        mailSender.send(message);

        return number;
    }
}