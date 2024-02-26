package org.example.stockswiftservice.domain.schedule.repository;

import org.example.stockswiftservice.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
