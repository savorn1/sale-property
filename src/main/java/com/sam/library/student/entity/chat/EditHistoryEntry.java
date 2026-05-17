package com.sam.library.student.entity.chat;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditHistoryEntry {
    private String content;
    private LocalDateTime editedAt;
}
