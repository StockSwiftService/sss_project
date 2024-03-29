package org.example.stockswiftservice.domain.schedule.service;

import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.example.stockswiftservice.domain.schedule.entity.Schedule;
import org.example.stockswiftservice.domain.schedule.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public List<Schedule> getList(String companyCode) {
        return this.scheduleRepository.findAllByCompanyCode(companyCode);
    }
    public Schedule getListById(Long id) {
        return this.scheduleRepository.findById(id).orElseThrow();
    }
    public Schedule create(Member member , String companyCode, String subject, String content, LocalDate startDate, LocalDate endDate) {
        Schedule schedule = Schedule.builder()
                .member(member)
                .companyCode(companyCode)
                .subject(subject)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .createDate(LocalDateTime.now())
                .build();

        return this.scheduleRepository.save(schedule);
    }
    public Schedule modify(Member member, Schedule schedule, String subject, String content, LocalDate startDate, LocalDate endDate) {
        if (member.getId().equals(schedule.getMember().getId())) {
            schedule.setSubject(subject);
            schedule.setContent(content);
            schedule.setStartDate(startDate);
            schedule.setEndDate(endDate);
            return this.scheduleRepository.save(schedule);
        } else {
            throw new RuntimeException("회원 정보가 일치하지 않습니다");
        }
    }
    public void delete(Member member, Schedule schedule) {
        if (member.getId().equals(schedule.getMember().getId())) {
            this.scheduleRepository.delete(schedule);
        } else {
            throw new RuntimeException("회원 정보가 일치하지 않습니다");
        }
    }

}
