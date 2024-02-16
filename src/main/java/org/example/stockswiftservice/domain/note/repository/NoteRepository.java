package org.example.stockswiftservice.domain.note.repository;

import org.example.stockswiftservice.domain.note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
//    List<Note> findNotesByUsername(String username);
}
