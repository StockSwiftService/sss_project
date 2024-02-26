package org.example.stockswiftservice.domain.question.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.question.dto.EmailPostDto;
import org.example.stockswiftservice.domain.question.entity.Question;
import org.example.stockswiftservice.domain.question.entity.QuestionMessage;
import org.example.stockswiftservice.domain.question.service.QuestionService;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

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
    public RsData<QuestionsResponse> adminGetQuestions(@RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Question> questions = this.questionService.getQuestions(page);
        return RsData.of("S-1", "문의 리스트 불러오기 성공", new QuestionsResponse(questions));
    }
    @GetMapping("/admin/{id}")
    public RsData<QuestionResponse> adminGetQuestion(@PathVariable("id") Long id) {
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
    public RsData<QuestionResponse> adminCreateQuestion(@Valid @RequestBody QuestionRequest questionRequest) {
        Question question = this.questionService.createQuestion(questionRequest.getSubject(), questionRequest.getContent(), questionRequest.getIsPublic());
        return RsData.of("S-2", "문의 생성 성공", new QuestionResponse(question));
    }
    @PatchMapping("/admin/{id}")
    public RsData<QuestionResponse> adminModifyQuestion(@PathVariable("id") Long id, @Valid @RequestBody QuestionRequest questionRequest) {
        Question question = this.questionService.getQuestion(id);
        Question modifyQuestion = this.questionService.modifyQuestion(question, questionRequest.getSubject(), questionRequest.getContent(), questionRequest.getIsPublic());
        return RsData.of("S-3", "문의 수정 성공", new QuestionResponse(modifyQuestion));
    }
//    @Getter
//    @AllArgsConstructor
//    public static class PublicResponse {
//        private Boolean isPublic;
//    }
//    @GetMapping("/admin/isPublic/{id}")
//    public RsData<PublicResponse> adminSetPublic(@PathVariable("id") Long id) {
//        Question question = this.questionService.getQuestion(id);
//        Boolean isPublic = this.questionService.setQuestionPublic(question);
//        if (!isPublic) {
//            return RsData.of("S-7.1", "비공개", new PublicResponse(false));
//        }
//        return RsData.of("S-7.2", "공개", new PublicResponse(true));
//    } 필요 없어진듯
    @DeleteMapping("/admin/{id}")
    public RsData<QuestionResponse> adminDeleteQuestion(@PathVariable("id") Long id) {
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
    public RsData<QuestionsDeleteResponse> adminDeleteCheckedQuestion(@RequestBody DeleteRequest deleteRequest) {
        List<Question> questions = this.questionService.findAllByManyId(deleteRequest.getQuestionIds());
        this.questionService.deleteCheckedQuestion(deleteRequest.getQuestionIds());
        return RsData.of("S-4.1", "문의 삭제 성공", new QuestionsDeleteResponse(questions));
    }
//    @GetMapping("")
//    public RsData<QuestionsResponse> getQuestionList(@RequestParam(value = "page", defaultValue = "0") int page) {
//        Page<Question> questions = this.questionService.getQuestionsIfPublic(page);
//        return RsData.of("S-5", "공개설정된 문의글 리스트 불러오기 성공", new QuestionsResponse(questions));
//    }

    @GetMapping("")
    public RsData<QuestionsResponse> getQuestionSearchList(@RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Question> questions = this.questionService.getSearchQuestionsByIsPublic(kw, page);
        return RsData.of("S-6", "공개설정된 문의글에서 검색한 리스트 불러오기 성공", new QuestionsResponse(questions));
    }
    @PostMapping("/contact")
    public ResponseEntity<String> sendQuestion(@Valid @RequestBody EmailPostDto emailPostDto) {
        QuestionMessage questionMessage = QuestionMessage.builder()
                .from(emailPostDto.getEmail())
                .subject(emailPostDto.getSubject())
                .content(emailPostDto.getContent())
                .build();
        questionService.sendMail(questionMessage, "email");

        return ResponseEntity.ok("1대1 문의 발송 성공");
    }
}
