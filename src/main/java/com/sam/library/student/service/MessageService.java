package com.sam.library.student.service;

import com.sam.library.student.dto.chat.AddReactionDTO;
import com.sam.library.student.dto.chat.EditMessageDTO;
import com.sam.library.student.dto.chat.SendMessageDTO;
import com.sam.library.student.entity.chat.Message;
import com.sam.library.student.entity.chat.MessageReaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    Message sendMessage(SendMessageDTO dto);

    Message getMessageById(Long id);

    Page<Message> getMessages(Long conversationId, Pageable pageable);

    Message editMessage(Long messageId, EditMessageDTO dto);

    void deleteMessage(Long messageId);

    MessageReaction addReaction(Long messageId, AddReactionDTO dto);

    void removeReaction(Long messageId, String emoji);

    List<MessageReaction> getReactions(Long messageId);

    void markDelivered(Long messageId);

    void markRead(Long messageId);
}
