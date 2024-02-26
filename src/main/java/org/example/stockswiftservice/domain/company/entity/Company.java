package org.example.stockswiftservice.domain.company.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.example.stockswiftservice.global.baseentity.BaseEntity;

import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Company extends BaseEntity {

    //기업명
    private String name;

    //사업자번호
    @Column(unique = true)
    private String businessNumber;

    // 대표 이름
    private String repName;

    //회사 이메일(대표 or 기업메일)
    @Column(unique = true)
    private String email;

    //기업 코드
    @Column(unique = true)
    private String companyCode;

    //주소
    private String address;

    //상세주소
    private String detailAddress;

    //승인여부
    @Setter
    private boolean isApproved = false;

    @Setter
    private String memo;

    //사원
    @OneToMany
    private Set<Member> EmployeeList;

}
