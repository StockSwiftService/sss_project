package org.example.stockswiftservice.domain.question.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.example.stockswiftservice.domain.member.service.MemberService;
import org.example.stockswiftservice.domain.question.dto.EmailPostDto;
import org.example.stockswiftservice.domain.question.entity.Question;
import org.example.stockswiftservice.domain.question.entity.QuestionMessage;
import org.example.stockswiftservice.domain.question.service.QuestionService;
import org.example.stockswiftservice.global.jwt.JwtProvider;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.stockswiftservice.domain.global.filter.JwtAuthorizationFilter.extractAccessToken;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Getter
    @AllArgsConstructor
    public static class QuestionsResponse{
        private final Page<Question> questions;
    }
    @Getter
    @AllArgsConstructor
    public static class QuestionResponse{
        private final Question question;
    }

    @GetMapping("/admin")
    public RsData<QuestionsResponse> adminGetQuestions(@RequestParam(value = "page", defaultValue = "0") int page, HttpServletRequest request) {
        String token = extractAccessToken(request);
        int authority = ((Integer) jwtProvider.getClaims(token).get("authority"));
        if (authority != 1) {
            return RsData.of("AS-1", "관리자만 접속 가능", null);
        }
        Page<Question> questions = this.questionService.getQuestions(page);
        return RsData.of("S-1", "문의 리스트 불러오기 성공", new QuestionsResponse(questions));
    }
    @GetMapping("/admin/{id}")
    public RsData<QuestionResponse> adminGetQuestion(@PathVariable("id") Long id, HttpServletRequest request) {
        String token = extractAccessToken(request);
        int authority = ((Integer) jwtProvider.getClaims(token).get("authority"));
        if (authority != 1) {
            return RsData.of("AS-7", "관리자만 접속 가능", null);
        }
        Question question = this.questionService.getQuestion(id);
        return RsData.of("S-7", "문의 불러오기 성공", new QuestionResponse(question));
    }
    @Data
    public static class QuestionRequest {
        @NotBlank
        private String subject;
        @NotBlank
        private String content;
        @NotNull
        private Boolean isPublic;
    }
    @PostMapping("/admin")
    public RsData<QuestionResponse> adminCreateQuestion(@Valid @RequestBody QuestionRequest questionRequest, HttpServletRequest request) {
        String token = extractAccessToken(request);
        int authority = ((Integer) jwtProvider.getClaims(token).get("authority"));
        if (authority != 1) {
            return RsData.of("AS-2", "관리자만 접속 가능", null);
        }
        Question question = this.questionService.createQuestion(questionRequest.getSubject(), questionRequest.getContent(), questionRequest.getIsPublic());
        return RsData.of("S-2", "문의 생성 성공", new QuestionResponse(question));
    }
    @PatchMapping("/admin/{id}")
    public RsData<QuestionResponse> adminModifyQuestion(@PathVariable("id") Long id, @Valid @RequestBody QuestionRequest questionRequest, HttpServletRequest request) {
        String token = extractAccessToken(request);
        int authority = ((Integer) jwtProvider.getClaims(token).get("authority"));
        if (authority != 1) {
            return RsData.of("AS-3", "관리자만 접속 가능", null);
        }
        Question question = this.questionService.getQuestion(id);
        Question modifyQuestion = this.questionService.modifyQuestion(question, questionRequest.getSubject(), questionRequest.getContent(), questionRequest.getIsPublic());
        return RsData.of("S-3", "문의 수정 성공", new QuestionResponse(modifyQuestion));
    }
    @DeleteMapping("/admin/{id}")
    public RsData<QuestionResponse> adminDeleteQuestion(@PathVariable("id") Long id, HttpServletRequest request) {
        String token = extractAccessToken(request);
        int authority = ((Integer) jwtProvider.getClaims(token).get("authority"));
        if (authority != 1) {
            return RsData.of("AS-4", "관리자만 접속 가능", null);
        }
        Question question = this.questionService.getQuestion(id);
        this.questionService.deleteQuestion(question);
        return RsData.of("S-4", "문의 삭제 성공", new QuestionResponse(question));
    }
    @Getter
    @AllArgsConstructor
    public static class QuestionsDeleteResponse{
        private final List<Question> questions;
    }
    @Data
    public static class DeleteRequest {
        private List<Long> questionIds;
    }
    @PostMapping("/admin/delete")
    public RsData<QuestionsDeleteResponse> adminDeleteCheckedQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        String token = extractAccessToken(request);
        int authority = ((Integer) jwtProvider.getClaims(token).get("authority"));
        if (authority != 1) {
            return RsData.of("AS-4.1", "관리자만 접속 가능", null);
        }
        List<Question> questions = this.questionService.findAllByManyId(deleteRequest.getQuestionIds());
        this.questionService.deleteCheckedQuestion(deleteRequest.getQuestionIds());
        return RsData.of("S-4.1", "문의 삭제 성공", new QuestionsDeleteResponse(questions));
    }

    @GetMapping("")
    public RsData<QuestionsResponse> getQuestionSearchList(@RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Question> questions = this.questionService.getSearchQuestionsByIsPublic(kw, page);
        return RsData.of("S-6", "공개설정된 문의글에서 검색한 리스트 불러오기 성공", new QuestionsResponse(questions));
    }
    @PostMapping("/contact")
    public ResponseEntity<String> sendQuestion(@Valid @RequestBody EmailPostDto emailPostDto, HttpServletRequest request) {
        String token = extractAccessToken(request);
        Long userId = ((Integer) jwtProvider.getClaims(token).get("id")).longValue();
        Member member = this.memberService.findbyId(userId).orElse(null);
        if (member != null) {
            QuestionMessage questionMessage = QuestionMessage.builder()
                    .from(emailPostDto.getEmail())
                    .subject(emailPostDto.getSubject())
                    .content(emailPostDto.getContent())
                    .build();
            questionService.sendMail(questionMessage, "email");

            return ResponseEntity.ok("1대1 문의 발송 성공");
        } else {
            return ResponseEntity.ok("로그인 후 이용해주세요");
        }

    }
}
