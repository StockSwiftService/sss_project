package org.example.stockswiftservice.domain.question.repository;

import org.example.stockswiftservice.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findAll(Pageable pageable);
    Page<Question> findListByIsPublic(Boolean isPublic, Pageable pageable);
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);

}
