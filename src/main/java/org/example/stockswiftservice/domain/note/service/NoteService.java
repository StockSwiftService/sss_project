package org.example.stockswiftservice.domain.note.service;

import lombok.RequiredArgsConstructor;
import org.example.stockswiftservice.domain.note.entity.Note;
import org.example.stockswiftservice.domain.note.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;

    public List<Note> getNoteList() {
        return this.noteRepository.findAll();
    }
    public Note getNoteById(Long id) {
        return this.noteRepository.findById(id).orElseThrow
                (() -> new RuntimeException("해당 쪽지가 존재하지 않습니다"));
    }
    public Note createNote(String content, String receiver) {
        Note note = Note.builder()
                .content(content)
//                .sender(sender)
                .receiver(receiver)
                .isChecked(false)
                .createDate(LocalDateTime.now())
                .build();
        return this.noteRepository.save(note);
    }
//    public Note modifyNote(Note note, String content) {
//        note.setContent(content);
//        return this.noteRepository.save(note);
//    }
    public void deleteNote(Note note) {
        this.noteRepository.delete(note);
    }
    public Boolean getNoteIfChecked(Note note) {
        if (!note.getIsChecked()) {
            return false;
        } else {
            return true;
        }
    }
    public Boolean checkNote(Note note) {
        if (!note.getIsChecked()) {
            note.setIsChecked(true);
            this.noteRepository.save(note);
            return true;
        } else {
            return false;
        }
    }
    public Integer countIfChecked() {
        List<Note> notes = this.noteRepository.findAll();
        int count = 0;
        for(Note note : notes) {
            if (!note.getIsChecked()) {
                count++;
            }
        }
        return count;
    }

    public List<List<Note>> getCheckedList() {
        List<Note> notes = this.noteRepository.findAll();
        List<Note> checkedNoteList = new ArrayList<>();
        List<Note> unCheckedNoteList = new ArrayList<>();
        for (Note note : notes) {
            if (note.getIsChecked()) {
                checkedNoteList.add(note);
            } else if(!note.getIsChecked()) {
                unCheckedNoteList.add(note);
            }
        }
        List<List<Note>> result = new ArrayList<>();
        result.add(checkedNoteList);
        result.add(unCheckedNoteList);
        return result;
    }
    public Boolean deleteAfterDate(Note note) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime sentDate = note.getCreateDate();
        Duration duration = Duration.between(sentDate, today);
        Long durationToDay = duration.toDays();

        if (durationToDay >= 7) {
            this.noteRepository.delete(note);
            return true;
        } else
            return false;
    }
//    public List<Note> findNotesBySender() {
//        this.noteRepository.findNotesByUsername()
//    }
}
