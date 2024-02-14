package org.example.stockswiftservice.domain.company.service;

import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.company.entity.Company;
import org.example.stockswiftservice.domain.company.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    public String newCode(){
        while (true) {
            String str = "";
            char[] charSet = new char[]{'A', 'B', 'C', 'D', 'E', 'F',
                    'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                    'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            int idx = 0;
            for (int i = 0; i < 6; i++) {
                idx = (int) (charSet.length * Math.random());
                str += charSet[idx];
            }
            Company code = this.companyRepository.findByCompanyCode(str).orElse(null);
            if (code == null){
              return str;
            }
        }
    }

    public String join(String name, String businessNumber, String repName, String email) {
        String str = newCode();
        Company newCompany = Company.builder()
                .name(name)
                .businessNumber(businessNumber)
                .repName(repName)
                .email(email)
                .companyCode(str)
                .build();

        this.companyRepository.save(newCompany);


        return str;
    }
}
