package com.sam.library.student.dto.chat;

import com.sam.library.student.enums.ConversationType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateConversationDTO {

    @NotNull
    private ConversationType type;

    private String name;
    private String avatar;

    private List<Long> memberIds;
}
