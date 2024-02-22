package org.example.stockswiftservice.domain.question.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stockswiftservice.domain.question.entity.Question;
import org.example.stockswiftservice.domain.question.entity.QuestionMessage;
import org.example.stockswiftservice.domain.question.repository.QuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final JavaMailSender javaMailSender;

    public Page<Question> getQuestions(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAll(pageable);
    }
    public Question getQuestion(Long id) {
        return this.questionRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 문의가 존재하지 않습니다"));
    }
    public Question createQuestion(String subject, String content, Boolean isPublic) {
        Question question = Question.builder()
                .subject(subject)
                .content(content)
                .isPublic(isPublic)
                .createDate(LocalDateTime.now())
                .build();
        return this.questionRepository.save(question);
    }
    public Question modifyQuestion(Question question, String subject, String content, Boolean isPublic) {
        question.setSubject(subject);
        question.setContent(content);
        question.setIsPublic(isPublic);
        return this.questionRepository.save(question);
    }
//    public Boolean setQuestionPublic(Question question) {
//        if (question.getIsPublic()) {
//            return true;
//        } else {
//            return false;
//        }
//    } 필요 없어진듯
    public void deleteQuestion(Question question) {
        this.questionRepository.delete(question);
    }
    public Page<Question> getQuestionsIfPublic(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<Question> questionsPage = this.questionRepository.findListByIsPublic(true, pageable);
        if (questionsPage.isEmpty()) {
            throw new RuntimeException("공개된 문의글이 존재하지 않습니다.");
        }
        return questionsPage;
    }
    public Page<Question> getSearchQuestionsByIsPublic(String kw, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
    }

    public Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Question> a, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                return  cb.and(
                        cb.isTrue(a.get("isPublic")),
                        cb.or(
                                cb.like(a.get("subject"), "%" + kw + "%"),
                                cb.like(a.get("content"), "%" + kw + "%")
                        )
                );
            }
        };
    }

    public Boolean sendMail(QuestionMessage questionMessage, String type) {
        String htmlContent = createHtmlContent(questionMessage.getFrom(), questionMessage.getContent(), type);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setFrom(questionMessage.getFrom()); // 메일 발신자
            mimeMessageHelper.setTo("jhseodevelopment@gmail.com");
            mimeMessageHelper.setSubject(questionMessage.getSubject()); // 메일 제목
            mimeMessageHelper.setText(htmlContent, true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            log.info("Success");
            return true;

        } catch (MessagingException e) {
            log.info("fail");
            throw new RuntimeException(e);
        }
    }
    public String createHtmlContent(String from, String content, String type) {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html>");
        htmlContent.append("<body>");
        htmlContent.append("<div style=\"margin:100px;\">");
        htmlContent.append("<h1> 안녕하세요.</h1>");
        htmlContent.append("<h1>").append(from).append("의 문의 내용입니다.</h1>");
        htmlContent.append("<div style=\"border: 1px solid black; font-family:verdana; padding: 10px;\">");
        htmlContent.append("<h3 astyle=\"color:black\"> 1대1 문의 내용입니다. </h3>");
        htmlContent.append("<br/>");
        htmlContent.append("<div> <strong>내용</strong> </div>");
        htmlContent.append("<br/>");
        htmlContent.append("<div>").append(content).append("</div>");
        htmlContent.append("</div>");
        htmlContent.append("<br/>");
        htmlContent.append("</div>");
        htmlContent.append("</body>");
        htmlContent.append("</html>");

        return htmlContent.toString();
    }
    public List<Question> findAllByManyId(List<Long> id) {
        return this.questionRepository.findAllById(id);
    }
    public void deleteCheckedQuestion(List<Long> id) {
        this.questionRepository.deleteAllById(id);
    }
}
