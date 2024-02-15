package org.example.stockswiftservice.domain.note.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.note.entity.Note;
import org.example.stockswiftservice.domain.note.service.NoteService;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
//@RequestMapping(value = "/api/v1/notes", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;
    @Getter
    @AllArgsConstructor
    public static class NotesResponse {
        private final List<Note> notes;
    }
    @Getter
    @AllArgsConstructor
    public static class NoteResponse {
        private final Note note;
    }
    @GetMapping("")
    public RsData<NotesResponse> noteList() {
        List<Note> notes = this.noteService.getNoteList();
        return RsData.of("S-1", "전체 쪽지 리스트 불러오기 성공", new NotesResponse(notes));
    }
    @GetMapping("/{id}")
    public RsData<NoteResponse> getNote(@PathVariable("id") Long id) {
        Note note = this.noteService.getNoteById(id);
        return RsData.of("S-2", "쪽지 불러오기 성공", new NoteResponse(note));
    }
    @Data
    public static class NoteRequest {
        @NotEmpty
        private String content;
        @NotEmpty
        private String receiver;
    }
    @PostMapping("")
    public RsData<NoteResponse> createNote(@Valid NoteRequest noteRequest) {
        Note note = this.noteService.createNote(noteRequest.getContent(), noteRequest.getReceiver());
        return RsData.of("S-3", "쪽지 생성 성공", new NoteResponse(note));
    }
    @Data
    public static class NoteModifyRequest {
        private String content;
    }
    @PatchMapping("/{id}")
    public RsData<NoteResponse> modifyNote(@Valid NoteModifyRequest noteModifyRequest, @PathVariable("id") Long id) {
        Note note = this.noteService.getNoteById(id);
        Note modifyNote = this.noteService.modifyNote(note, noteModifyRequest.getContent());
        return RsData.of("S-4", "쪽지 수정 성공", new NoteResponse(modifyNote));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable("id") Long id) {
        Note note = this.noteService.getNoteById(id);
        this.noteService.deleteNote(note);
        return ResponseEntity.ok("삭제 성공");
    }
    @PatchMapping("/{id}/check")
    public ResponseEntity<String> checkNote(@PathVariable("id") Long id) {
        Note note = this.noteService.getNoteById(id);
        Boolean checkNote = this.noteService.checkNote(note);
        if (!checkNote) {
            return ResponseEntity.ok("이미 확인한 쪽지입니다");
        }
        return ResponseEntity.ok("쪽지를 확인하였습니다");
    }
    @GetMapping("/count")
    public ResponseEntity<String> countCheckNotes() {
        Integer count = this.noteService.countIfChecked();
        return ResponseEntity.ok(String.valueOf(count));
    }
}
