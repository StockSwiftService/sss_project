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
    public static void createNumber() {
        //(int) Math.random() * (최댓값-최소값+1) + 최소값
        number = (int) (Math.random() * (90000)) + 10000;
    }

    // 이메일 인증을 하기 위한 createEmail 구문
    public MimeMessage createEmail(String email) {
        createNumber();
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("Stock Swift Service 이메일 인증");
            String body = "";
            body += "<h1>" + "안녕하세요 Stock Swift Service 입니다." + "</h1>";
            body += "<h2>" + "아래 인증코드를 입력해 주세요." + "</h2>";
            body += "<h1>" + number + "</h1>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    public boolean isEmailDuplicate(String email) {
        Optional<Company> existingEmail = companyRepository.findByEmail(email);
        return existingEmail.isPresent(); // true면 중복, false면 중복 아님
    }

    public int sendEmail(String email) {
        MimeMessage message = createEmail(email);
        mailSender.send(message);

        return number;
    }

    public void approveMail(String email, String companyCode) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("Stock Swift Service 회원 가입을 환영합니다!");
            String body = "";
            body += "<h1>" + "안녕하세요 Stock Swift Service 입니다." + "</h1>";
            body += "<h2>" + "승인이 성공적으로 완료되었습니다. 가입을 진심으로 감사드립니다." + "</h2>";
            body += "<h1>" + "고객님의 회사 코드는 " + companyCode + " 입니다." + "</h1>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(message);
    }

    public void disapproveMail(String email) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("[Stock Swift Service 승인 취소 알림 메일]");
            String body = "";
            body += "<h1>" + "안녕하세요 Stock Swift Service 입니다." + "</br>" + "</h1>";
            body += "<h2>" + "귀하의 승인이 취소되었습니다. 이에 따른 사유에 대해서는 관리자에게 문의해 주시기 바랍니다.\n" + "</h2>" +
                    "<h3>" +
                    "저희 팀은 항상 최선을 다해 귀하의 요청을 처리해 드리기 위해 노력하고 있습니다. 그러나 때로는 예상치 못한 상황이 발생할 수 있음을 양해해 주시기 바랍니다.\n" +
                    "</h3>" +
                    "<h3>" +
                    "더 나은 서비스를 제공하기 위해 저희는 꾸준한 노력을 기울이고 있으며, 귀하의 피드백은 저희에게 소중한 자산입니다. 더 나은 결과물을 위해 노력하고자 합니다.\n" + "</h3>" +
                    "</br>" +
                    "<h3>" +
                    "감사합니다." + "</br>" + "</h3>";
            body += "<h3>" + "이메일 : SSS@gmail.com 담당자 홍길동 " + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(message);
    }
}