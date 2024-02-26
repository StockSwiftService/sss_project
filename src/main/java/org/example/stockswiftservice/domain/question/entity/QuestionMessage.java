package org.example.stockswiftservice.domain.question.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionMessage {
    private String from;
    private String subject;
    private String content;
}
