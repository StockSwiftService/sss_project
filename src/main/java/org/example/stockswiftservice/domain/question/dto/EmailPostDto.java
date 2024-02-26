package org.example.stockswiftservice.domain.question.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailPostDto {
    @Email
    private String email;
    private String subject;
    private String content;
}
