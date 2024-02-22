package org.example.stockswiftservice.domain.question.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.stockswiftservice.global.baseentity.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class Question extends BaseEntity {
    @Column(length = 30)
    private String subject;
    private String content;
    private Boolean isPublic;
}
