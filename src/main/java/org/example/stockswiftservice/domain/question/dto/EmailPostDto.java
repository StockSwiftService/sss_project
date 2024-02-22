package org.example.stockswiftservice.domain.question.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailPostDto {
    private String email;
    private String subject;
    private String content;
}
