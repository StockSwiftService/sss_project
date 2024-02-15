package org.example.stockswiftservice.domain.schedule.service;

import lombok.RequiredArgsConstructor;
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

    public List<Schedule> getList() {
        return this.scheduleRepository.findAll();
    }
    public Schedule getListById(Long id) {
        return this.scheduleRepository.findById(id).orElseThrow();
    }
    public Schedule create(String subject, String content, LocalDate startDate, LocalDate endDate) {
        Schedule schedule = Schedule.builder()
                .subject(subject)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .createDate(LocalDateTime.now())
                .build();

        return this.scheduleRepository.save(schedule);
    }
    public Schedule modify(Schedule schedule, String subject, String content, LocalDate startDate, LocalDate endDate) {
        schedule.setSubject(subject);
        schedule.setContent(content);
        schedule.setStartDate(startDate);
        schedule.setEndDate(endDate);
        return this.scheduleRepository.save(schedule);
    }
    public void delete(Schedule schedule) {
        this.scheduleRepository.delete(schedule);
    }
}
