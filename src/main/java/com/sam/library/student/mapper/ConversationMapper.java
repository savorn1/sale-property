package com.sam.library.student.mapper;

import com.sam.library.student.dto.chat.ConversationDTO;
import com.sam.library.student.dto.chat.MessageReactionDTO;
import com.sam.library.student.dto.chat.MessageReminderDTO;
import com.sam.library.student.dto.chat.SavedReplyDTO;
import com.sam.library.student.dto.chat.ScheduledMessageDTO;
import com.sam.library.student.dto.chat.UserConversationDTO;
import com.sam.library.student.dto.chat.ParticipantDTO;
import com.sam.library.student.entity.SysUser;
import com.sam.library.student.entity.chat.*;
import com.sam.library.student.repository.ConversationMemberRepository;
import com.sam.library.student.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConversationMapper {

    private final ConversationMemberRepository memberRepo;
    private final SysUserRepository userRepo;

    public ConversationDTO toDTO(Conversation c) {
        ConversationDTO dto = new ConversationDTO();
        dto.setId(c.getId());
        dto.setType(c.getType());
        dto.setName(c.getName());
        dto.setAvatar(c.getAvatar());
        dto.setCreatedBy(c.getCreatedBy());
        dto.setLastMessage(c.getLastMessage());
        dto.setPinnedMessages(c.getPinnedMessages());
        dto.setDisappearingMessages(c.getDisappearingMessages());
        dto.setInviteToken(c.getInviteToken());

        List<Long> memberIds = memberRepo.findByConversationId(c.getId())
                .stream()
                .map(ConversationMember::getUserId)
                .toList();

        Map<Long, SysUser> userMap = userRepo.findAllById(memberIds)
                .stream()
                .collect(Collectors.toMap(SysUser::getId, u -> u));

        List<ParticipantDTO> participants = memberIds.stream()
                .filter(userMap::containsKey)
                .map(id -> {
                    SysUser u = userMap.get(id);
                    return new ParticipantDTO(u.getId(), u.getUuid(), u.getName());
                })
                .toList();
        dto.setParticipants(participants);

        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());
        return dto;
    }

    public UserConversationDTO toUserConversationDTO(UserConversation uc) {
        UserConversationDTO dto = new UserConversationDTO();
        dto.setId(uc.getId());
        dto.setUserId(uc.getUserId());
        dto.setConversationId(uc.getConversationId());
        dto.setLastReadMessageId(uc.getLastReadMessageId());
        dto.setUnreadCount(uc.getUnreadCount());
        dto.setJoinedAt(uc.getJoinedAt());
        dto.setMuted(uc.isMuted());
        dto.setStarredMessageIds(uc.getStarredMessageIds());
        dto.setArchived(uc.isArchived());
        return dto;
    }

    public SavedReplyDTO toSavedReplyDTO(SavedReply sr) {
        SavedReplyDTO dto = new SavedReplyDTO();
        dto.setId(sr.getId());
        dto.setUserId(sr.getUserId());
        dto.setTitle(sr.getTitle());
        dto.setShortcut(sr.getShortcut());
        dto.setContent(sr.getContent());
        dto.setCreatedAt(sr.getCreatedAt());
        dto.setUpdatedAt(sr.getUpdatedAt());
        return dto;
    }

    public MessageReminderDTO toReminderDTO(MessageReminder r) {
        MessageReminderDTO dto = new MessageReminderDTO();
        dto.setId(r.getId());
        dto.setUserId(r.getUserId());
        dto.setMessageId(r.getMessageId());
        dto.setConversationId(r.getConversationId());
        dto.setRemindAt(r.getRemindAt());
        dto.setNote(r.getNote());
        dto.setMessageContent(r.getMessageContent());
        dto.setStatus(r.getStatus());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }

    public ScheduledMessageDTO toScheduledMessageDTO(ScheduledMessage sm) {
        ScheduledMessageDTO dto = new ScheduledMessageDTO();
        dto.setId(sm.getId());
        dto.setConversationId(sm.getConversationId());
        dto.setSenderId(sm.getSenderId());
        dto.setContent(sm.getContent());
        dto.setType(sm.getType());
        dto.setReplyTo(sm.getReplyTo());
        dto.setScheduledFor(sm.getScheduledFor());
        dto.setStatus(sm.getStatus());
        dto.setCreatedAt(sm.getCreatedAt());
        return dto;
    }

    public MessageReactionDTO toReactionDTO(MessageReaction r) {
        MessageReactionDTO dto = new MessageReactionDTO();
        dto.setId(r.getId());
        dto.setMessageId(r.getMessageId());
        dto.setUserId(r.getUserId());
        dto.setEmoji(r.getEmoji());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
