package org.example.stockswiftservice.domain.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.stockswiftservice.domain.company.entity.Company;
import org.example.stockswiftservice.global.baseentity.BaseEntity;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Member extends BaseEntity {

    //이름
    private String name;

    //직급
    private String rank;

    //아이디
    private String username;

    //비밀번호
    private String password;

    //생일
    private LocalDate birthday;

    //권한
    private int authority;

    //회사
    @ManyToOne
    private Company company;

}
