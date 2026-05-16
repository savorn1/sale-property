package com.sam.library.student.service;

import com.sam.library.student.dto.chat.*;
import com.sam.library.student.entity.chat.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConversationService {

    Conversation createConversation(CreateConversationDTO dto);

    Conversation getConversationById(Long id);

    Page<Conversation> getMyConversations(Pageable pageable);

    void addMembers(Long conversationId, AddMembersDTO dto);

    void removeMember(Long conversationId, Long userId);

    List<ConversationMember> getMembers(Long conversationId);

    Conversation joinByInviteToken(String token);

    UserConversation getOrCreateUserConversation(Long userId, Long conversationId);

    UserConversation markAsRead(Long conversationId, Long messageId);

    UserConversation toggleMute(Long conversationId);

    UserConversation toggleArchive(Long conversationId);

    UserConversation starMessage(Long conversationId, Long messageId);

    UserConversation unstarMessage(Long conversationId, Long messageId);

    List<SavedReply> getSavedReplies();

    SavedReply createSavedReply(CreateSavedReplyDTO dto);

    void deleteSavedReply(Long id);

    List<MessageReminder> getReminders();

    MessageReminder createReminder(CreateMessageReminderDTO dto);

    void cancelReminder(Long id);

    List<ScheduledMessage> getScheduledMessages(Long conversationId);

    ScheduledMessage createScheduledMessage(CreateScheduledMessageDTO dto);

    void cancelScheduledMessage(Long id);
}
