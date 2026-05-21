package com.sam.library.student.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sam.library.student.common.UserContext;
import com.sam.library.student.dto.chat.AddMembersDTO;
import com.sam.library.student.dto.chat.CreateConversationDTO;
import com.sam.library.student.dto.chat.CreateMessageReminderDTO;
import com.sam.library.student.dto.chat.CreateSavedReplyDTO;
import com.sam.library.student.dto.chat.CreateScheduledMessageDTO;
import com.sam.library.student.entity.chat.Conversation;
import com.sam.library.student.entity.chat.ConversationMember;
import com.sam.library.student.entity.chat.MessageReminder;
import com.sam.library.student.entity.chat.SavedReply;
import com.sam.library.student.entity.chat.ScheduledMessage;
import com.sam.library.student.entity.chat.UserConversation;
import com.sam.library.student.enums.ConversationType;
import com.sam.library.student.enums.MemberRole;
import com.sam.library.student.enums.ReminderStatus;
import com.sam.library.student.enums.ScheduledMessageStatus;
import com.sam.library.student.exception.AppException;
import com.sam.library.student.repository.ConversationMemberRepository;
import com.sam.library.student.repository.ConversationRepository;
import com.sam.library.student.repository.MessageReminderRepository;
import com.sam.library.student.repository.SavedReplyRepository;
import com.sam.library.student.repository.ScheduledMessageRepository;
import com.sam.library.student.repository.UserConversationRepository;
import com.sam.library.student.service.ConversationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepo;
    private final ConversationMemberRepository memberRepo;
    private final UserConversationRepository userConvRepo;
    private final SavedReplyRepository savedReplyRepo;
    private final MessageReminderRepository reminderRepo;
    private final ScheduledMessageRepository scheduledMsgRepo;

    @Override
    @Transactional
    public Conversation createConversation(CreateConversationDTO dto) {
        Long currentUserId = UserContext.getUserId();

        if (dto.getType() == ConversationType.PRIVATE) {
            List<Long> members = dto.getMemberIds();
            if (members == null || members.size() != 1) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Private conversation requires exactly one other member");
            }
            Long otherId = members.get(0);
            conversationRepo.findPrivateConversation(currentUserId, otherId)
                    .ifPresent(c -> { throw new AppException(HttpStatus.CONFLICT, "Private conversation already exists"); });
        }

        Conversation conv = new Conversation();
        conv.setType(dto.getType());
        conv.setName(dto.getName());
        conv.setAvatar(dto.getAvatar());
        conv.setCreatedBy(currentUserId);
        conv.setPinnedMessages(new ArrayList<>());

        if (dto.getType() == ConversationType.GROUP) {
            conv.setInviteToken(UUID.randomUUID().toString());
        }

        conv = conversationRepo.save(conv);

        addMemberInternal(conv.getId(), currentUserId, MemberRole.ADMIN);

        if (dto.getMemberIds() != null) {
            for (Long uid : dto.getMemberIds()) {
                if (!uid.equals(currentUserId)) {
                    addMemberInternal(conv.getId(), uid, MemberRole.MEMBER);
                }
            }
        }

        return conv;
    }

    @Override
    public Conversation getConversationById(Long id) {
        return conversationRepo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Conversation not found: " + id));
    }

    @Override
    public Page<Conversation> getMyConversations(Pageable pageable) {
        return conversationRepo.findByMemberUserId(UserContext.getUserId(), pageable);
    }

    @Override
    @Transactional
    public void addMembers(Long conversationId, AddMembersDTO dto) {
        assertMemberOrAdmin(conversationId);
        for (Long uid : dto.getUserIds()) {
            if (!memberRepo.existsByConversationIdAndUserId(conversationId, uid)) {
                addMemberInternal(conversationId, uid, MemberRole.MEMBER);
            }
        }
    }

    @Override
    @Transactional
    public void removeMember(Long conversationId, Long userId) {
        assertAdmin(conversationId);
        memberRepo.deleteByConversationIdAndUserId(conversationId, userId);
        userConvRepo.findByUserIdAndConversationId(userId, conversationId)
                .ifPresent(userConvRepo::delete);
    }

    @Override
    public List<ConversationMember> getMembers(Long conversationId) {
        assertMemberOrAdmin(conversationId);
        return memberRepo.findByConversationId(conversationId);
    }

    @Override
    @Transactional
    public Conversation joinByInviteToken(String token) {
        Conversation conv = conversationRepo.findByInviteToken(token)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Invalid invite token"));
        Long uid = UserContext.getUserId();
        if (!memberRepo.existsByConversationIdAndUserId(conv.getId(), uid)) {
            addMemberInternal(conv.getId(), uid, MemberRole.MEMBER);
        }
        return conv;
    }

    @Override
    @Transactional
    public UserConversation getOrCreateUserConversation(Long userId, Long conversationId) {
        return userConvRepo.findByUserIdAndConversationId(userId, conversationId)
                .orElseGet(() -> {
                    UserConversation uc = new UserConversation();
                    uc.setUserId(userId);
                    uc.setConversationId(conversationId);
                    uc.setJoinedAt(LocalDateTime.now());
                    uc.setStarredMessageIds(new ArrayList<>());
                    return userConvRepo.save(uc);
                });
    }

    @Override
    @Transactional
    public UserConversation markAsRead(Long conversationId, Long messageId) {
        Long uid = UserContext.getUserId();
        UserConversation uc = getOrCreateUserConversation(uid, conversationId);
        uc.setLastReadMessageId(messageId);
        uc.setUnreadCount(0);
        return userConvRepo.save(uc);
    }

    @Override
    @Transactional
    public UserConversation toggleMute(Long conversationId) {
        Long uid = UserContext.getUserId();
        UserConversation uc = getOrCreateUserConversation(uid, conversationId);
        uc.setMuted(!uc.isMuted());
        return userConvRepo.save(uc);
    }

    @Override
    @Transactional
    public UserConversation toggleArchive(Long conversationId) {
        Long uid = UserContext.getUserId();
        UserConversation uc = getOrCreateUserConversation(uid, conversationId);
        uc.setArchived(!uc.isArchived());
        return userConvRepo.save(uc);
    }

    @Override
    @Transactional
    public UserConversation starMessage(Long conversationId, Long messageId) {
        Long uid = UserContext.getUserId();
        UserConversation uc = getOrCreateUserConversation(uid, conversationId);
        if (!uc.getStarredMessageIds().contains(messageId)) {
            uc.getStarredMessageIds().add(messageId);
        }
        return userConvRepo.save(uc);
    }

    @Override
    @Transactional
    public UserConversation unstarMessage(Long conversationId, Long messageId) {
        Long uid = UserContext.getUserId();
        UserConversation uc = getOrCreateUserConversation(uid, conversationId);
        uc.getStarredMessageIds().remove(messageId);
        return userConvRepo.save(uc);
    }

    @Override
    public List<SavedReply> getSavedReplies() {
        return savedReplyRepo.findByUserId(UserContext.getUserId());
    }

    @Override
    @Transactional
    public SavedReply createSavedReply(CreateSavedReplyDTO dto) {
        Long uid = UserContext.getUserId();
        if (savedReplyRepo.existsByUserIdAndShortcut(uid, dto.getShortcut())) {
            throw new AppException(HttpStatus.CONFLICT, "Shortcut already exists: " + dto.getShortcut());
        }
        SavedReply reply = new SavedReply();
        reply.setUserId(uid);
        reply.setTitle(dto.getTitle());
        reply.setShortcut(dto.getShortcut());
        reply.setContent(dto.getContent());
        return savedReplyRepo.save(reply);
    }

    @Override
    @Transactional
    public void deleteSavedReply(Long id) {
        SavedReply reply = savedReplyRepo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Saved reply not found: " + id));
        if (!reply.getUserId().equals(UserContext.getUserId())) {
            throw new AppException(HttpStatus.FORBIDDEN, "Not your saved reply");
        }
        savedReplyRepo.delete(reply);
    }

    @Override
    public List<MessageReminder> getReminders() {
        return reminderRepo.findByUserIdAndStatus(UserContext.getUserId(),
                com.sam.library.student.enums.ReminderStatus.PENDING);
    }

    @Override
    @Transactional
    public MessageReminder createReminder(CreateMessageReminderDTO dto) {
        MessageReminder reminder = new MessageReminder();
        reminder.setUserId(UserContext.getUserId());
        reminder.setMessageId(dto.getMessageId());
        reminder.setConversationId(dto.getConversationId());
        reminder.setRemindAt(dto.getRemindAt());
        reminder.setNote(dto.getNote());
        reminder.setStatus(ReminderStatus.PENDING);
        return reminderRepo.save(reminder);
    }

    @Override
    @Transactional
    public void cancelReminder(Long id) {
        MessageReminder reminder = reminderRepo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Reminder not found: " + id));
        if (!reminder.getUserId().equals(UserContext.getUserId())) {
            throw new AppException(HttpStatus.FORBIDDEN, "Not your reminder");
        }
        reminder.setStatus(ReminderStatus.CANCELLED);
        reminderRepo.save(reminder);
    }

    @Override
    public List<ScheduledMessage> getScheduledMessages(Long conversationId) {
        return scheduledMsgRepo.findByConversationIdAndStatus(conversationId, ScheduledMessageStatus.PENDING);
    }

    @Override
    @Transactional
    public ScheduledMessage createScheduledMessage(CreateScheduledMessageDTO dto) {
        assertMemberOrAdmin(dto.getConversationId());
        ScheduledMessage msg = new ScheduledMessage();
        msg.setConversationId(dto.getConversationId());
        msg.setSenderId(UserContext.getUserId());
        msg.setContent(dto.getContent());
        msg.setType(dto.getType());
        msg.setReplyTo(dto.getReplyTo());
        msg.setScheduledFor(dto.getScheduledFor());
        msg.setStatus(ScheduledMessageStatus.PENDING);
        return scheduledMsgRepo.save(msg);
    }

    @Override
    @Transactional
    public void cancelScheduledMessage(Long id) {
        ScheduledMessage msg = scheduledMsgRepo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Scheduled message not found: " + id));
        if (!msg.getSenderId().equals(UserContext.getUserId())) {
            throw new AppException(HttpStatus.FORBIDDEN, "Not your scheduled message");
        }
        msg.setStatus(ScheduledMessageStatus.CANCELLED);
        scheduledMsgRepo.save(msg);
    }

    private void addMemberInternal(Long conversationId, Long userId, MemberRole role) {
        ConversationMember member = new ConversationMember();
        member.setConversationId(conversationId);
        member.setUserId(userId);
        member.setRole(role);
        member.setJoinedAt(LocalDateTime.now());
        memberRepo.save(member);
        getOrCreateUserConversation(userId, conversationId);
    }

    private void assertMemberOrAdmin(Long conversationId) {
        Long uid = UserContext.getUserId();
        if (!memberRepo.existsByConversationIdAndUserId(conversationId, uid)) {
            throw new AppException(HttpStatus.FORBIDDEN, "Not a member of this conversation");
        }
    }

    private void assertAdmin(Long conversationId) {
        Long uid = UserContext.getUserId();
        ConversationMember member = memberRepo.findByConversationIdAndUserId(conversationId, uid)
                .orElseThrow(() -> new AppException(HttpStatus.FORBIDDEN, "Not a member of this conversation"));
        if (member.getRole() != MemberRole.ADMIN) {
            throw new AppException(HttpStatus.FORBIDDEN, "Admin role required");
        }
    }
}
