package com.sam.library.student.dto.chat;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sam.library.student.entity.chat.DisappearingSettings;
import com.sam.library.student.entity.chat.LastMessageInfo;
import com.sam.library.student.entity.chat.PinnedMessage;
import com.sam.library.student.enums.ConversationType;

import lombok.Data;

@Data
public class ConversationDTO {
    private Long id;
    private ConversationType type;
    private String name;
    private String avatar;
    private Long createdBy;
    private LastMessageInfo lastMessage;
    private List<PinnedMessage> pinnedMessages;
    private DisappearingSettings disappearingMessages;
    private String inviteToken;
    private List<ParticipantDTO> participants;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}

