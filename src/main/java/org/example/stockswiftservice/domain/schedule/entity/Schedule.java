package org.example.stockswiftservice.domain.schedule.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.example.stockswiftservice.global.baseentity.BaseEntity;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class Schedule extends BaseEntity {
    private String subject;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private String companyCode;
    @ManyToOne
    private Member member;
}
