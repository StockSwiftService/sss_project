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
//    @Data
//    public static class NoteModifyRequest {
//        private String content;
//    }
//    @PatchMapping("/{id}")
//    public RsData<NoteResponse> modifyNote(@Valid NoteModifyRequest noteModifyRequest, @PathVariable("id") Long id) {
//        Note note = this.noteService.getNoteById(id);
//        Note modifyNote = this.noteService.modifyNote(note, noteModifyRequest.getContent());
//        return RsData.of("S-4", "쪽지 수정 성공", new NoteResponse(modifyNote));
//    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable("id") Long id) {
        // 삭제가 될때 sender와 receiver 한명이 삭제한다고해서 db에서 삭제되면 반대편 한명도 해당 쪽지가 사라지면 안됨
        // entity에 isDeletedBySender를 이용해 발신자가 삭제를 하면은 발신자 쪽지만 삭제되게 만들어야함
        // 반대로 isDeletedByReceiver를 이용해 수신자가 삭제를 하면은 수신자 측의 쪽지만 삭제되게 해야함
        // 이때 양쪽에서 둘다 삭제하면은 DB에서는 삭제를 해야하기 때문에 이 경우에는 DB에서 삭제처리하기
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
    @Getter
    @AllArgsConstructor
    public static class NotesListResponse {
        private List<Note> checkedList;
        private List<Note> unCheckedList;
    }
    @GetMapping("/checked")
    public RsData<NotesListResponse> getCheckedNotes() {
        List<List<Note>> result = this.noteService.getCheckedList();
        List<Note> checkedNote = result.get(0);
        List<Note> unCheckedNote = result.get(1);
        return RsData.of("S-4", "체크된 쪽지 리스트 불러오기 성공", new NotesListResponse(checkedNote, unCheckedNote));
    }

//    @GetMapping("/sent")
//    public RsData<NotesResponse> getSentNotes() {
//        sender를 유저네임으로 찾기
//    }
    @DeleteMapping("/{id}/deleteAfterDate")
    public ResponseEntity<String> deleteNoteAfterTime(@PathVariable("id") Long id) {
        Note note = this.noteService.getNoteById(id);
        Boolean deleteSuccess = this.noteService.deleteAfterDate(note);
        if (!deleteSuccess) {
            return ResponseEntity.ok("기간이 아직 만료되지 않았습니다");
        } else {
            return ResponseEntity.ok("1주일이 지나 쪽지가 만료되었습니다");
        }
        //로그인된 사용자에게 쪽지가 만료되었다고 쪽지를 보내야할듯
    }

}
