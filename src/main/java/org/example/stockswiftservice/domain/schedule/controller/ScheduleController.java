package org.example.stockswiftservice.domain.schedule.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.member.entity.Member;
import org.example.stockswiftservice.domain.member.service.MemberService;
import org.example.stockswiftservice.domain.schedule.entity.Schedule;
import org.example.stockswiftservice.domain.schedule.service.ScheduleService;
import org.example.stockswiftservice.global.jwt.JwtProvider;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.example.stockswiftservice.domain.global.filter.JwtAuthorizationFilter.extractAccessToken;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final JwtProvider jwtProvider;
    private final ScheduleService scheduleService;
    private final MemberService memberService;

    @Getter
    @AllArgsConstructor
    public static class SchedulesResponse {
        private List<Schedule> schedules;
    }

    @GetMapping("")
    public RsData<SchedulesResponse> scheduleList() {
        List<Schedule> schedules = this.scheduleService.getList();
        return RsData.of("S-1", "스케줄 목록 불러오기 성공", new SchedulesResponse(schedules));
    }

    @Getter
    @AllArgsConstructor
    public static class ScheduleResponse {
        private Schedule schedule;
    }
    @GetMapping("/{id}")
    public RsData<ScheduleResponse> getSchedule(@PathVariable("id") Long id) {
        Schedule schedule = this.scheduleService.getListById(id);
        return RsData.of("S-2", "스케쥴 불러오기 성공", new ScheduleResponse(schedule));
    }
    @Data
    public static class ScheduleRequest {
        @NotBlank
        private String subject;
        @NotBlank
        private String content;
        @NotNull
        private LocalDate startDate;
        @NotNull
        private LocalDate endDate;
    }
    @PostMapping("")
    public RsData<ScheduleResponse> createSchedule(@Valid @RequestBody ScheduleRequest scheduleRequest, HttpServletRequest request) {
        String token = extractAccessToken(request); //헤더에 담긴 쿠키에서 토큰 요청
        Long userId = ((Integer) jwtProvider.getClaims(token).get("id")).longValue(); //유저의 아이디 값
        Member member = this.memberService.findbyId(userId).orElse(null);
        Schedule schedule = this.scheduleService.create(member, scheduleRequest.getSubject(), scheduleRequest.getContent(), scheduleRequest.getStartDate(), scheduleRequest.getEndDate());
        return RsData.of("S-3", "스케쥴 생성 성공", new ScheduleResponse(schedule));
    }
    @PatchMapping("/{id}")
    public RsData<ScheduleResponse> modifySchedule(@PathVariable("id") Long id, @Valid @RequestBody ScheduleRequest scheduleRequest, HttpServletRequest request) {
        String token = extractAccessToken(request); //헤더에 담긴 쿠키에서 토큰 요청
        Long userId = ((Integer) jwtProvider.getClaims(token).get("id")).longValue(); //유저의 아이디 값
        Member member = this.memberService.findbyId(userId).orElse(null);
        Schedule schedule = this.scheduleService.getListById(id);
        this.scheduleService.modify(member, schedule, scheduleRequest.getSubject(), scheduleRequest.getContent(), scheduleRequest.getStartDate(), scheduleRequest.getEndDate());
        return RsData.of("S-4", "스케쥴 수정 성공", new ScheduleResponse(schedule));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable("id") Long id, HttpServletRequest request) {
        String token = extractAccessToken(request); //헤더에 담긴 쿠키에서 토큰 요청
        Long userId = ((Integer) jwtProvider.getClaims(token).get("id")).longValue(); //유저의 아이디 값
        Member member = this.memberService.findbyId(userId).orElse(null);
        Schedule schedule = this.scheduleService.getListById(id);
        this.scheduleService.delete(member, schedule);
        return ResponseEntity.ok("성공");
    }
    @AllArgsConstructor
    @Getter
    public static class UserResponse {
        public Boolean isConfirm;
    }
    @GetMapping("/check/{id}")
    public RsData<UserResponse> checkUser(HttpServletRequest request, @PathVariable("id") Long id){
        String token = extractAccessToken(request);
        Long userId = ((Integer) jwtProvider.getClaims(token).get("id")).longValue();
        Member member = this.memberService.findbyId(userId).orElse(null);
        Schedule schedule = this.scheduleService.getListById(id);
        if (member.getId().equals(schedule.getMember().getId())) {
            return RsData.of("S-5", "일치", new UserResponse(true));
        } else return RsData.of("S-6", "불일치", new UserResponse(false));
    }
}
