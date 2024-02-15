package org.example.stockswiftservice.domain.note.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.stockswiftservice.global.baseentity.BaseEntity;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Note extends BaseEntity {
    private String content;
    private String sender;
    private String receiver;
    private Boolean isChecked;
}
