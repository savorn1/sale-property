package com.sam.library.student.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PinnedMessage {
    private Long messageId;
    private Long pinnedBy;
    private LocalDateTime pinnedAt;
    private String content;
}
