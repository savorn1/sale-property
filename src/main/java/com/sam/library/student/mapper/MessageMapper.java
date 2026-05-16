package com.sam.library.student.mapper;

import com.sam.library.student.dto.chat.MessageDTO;
import com.sam.library.student.entity.chat.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageDTO toDTO(Message m) {
        MessageDTO dto = new MessageDTO();
        dto.setId(m.getId());
        dto.setConversationId(m.getConversationId());
        dto.setSenderId(m.getSenderId());
        dto.setType(m.getType());
        dto.setContent(m.getContent());
        dto.setAttachments(m.getAttachments());
        dto.setReplyTo(m.getReplyTo());
        dto.setDeleted(m.isDeleted());
        dto.setDeletedAt(m.getDeletedAt());
        dto.setEditedAt(m.getEditedAt());
        dto.setExpiresAt(m.getExpiresAt());
        dto.setPoll(m.getPoll());
        dto.setEditHistory(m.getEditHistory());
        dto.setForwardedFrom(m.getForwardedFrom());
        dto.setMentions(m.getMentions());
        dto.setCreatedAt(m.getCreatedAt());
        dto.setUpdatedAt(m.getUpdatedAt());
        return dto;
    }
}
