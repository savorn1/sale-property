package com.sam.library.student.service.impl;

import com.sam.library.student.common.UserContext;
import com.sam.library.student.dto.chat.AddReactionDTO;
import com.sam.library.student.dto.chat.EditMessageDTO;
import com.sam.library.student.dto.chat.SendMessageDTO;
import com.sam.library.student.entity.chat.*;
import com.sam.library.student.exception.AppException;
import com.sam.library.student.repository.*;
import com.sam.library.student.service.ConversationService;
import com.sam.library.student.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepo;
    private final MessageReactionRepository reactionRepo;
    private final MessageReceiptRepository receiptRepo;
    private final ConversationRepository conversationRepo;
    private final ConversationMemberRepository memberRepo;
    private final UserConversationRepository userConvRepo;
    private final ConversationService conversationService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public Message sendMessage(SendMessageDTO dto) {
        Long uid = UserContext.getUserId();
        assertMember(dto.getConversationId(), uid);

        Message msg = new Message();
        msg.setConversationId(dto.getConversationId());
        msg.setSenderId(uid);
        msg.setType(dto.getType() != null ? dto.getType() : com.sam.library.student.enums.MessageType.TEXT);
        msg.setContent(dto.getContent());
        msg.setAttachments(dto.getAttachments() != null ? dto.getAttachments() : new ArrayList<>());
        msg.setReplyTo(dto.getReplyTo());
        msg.setExpiresAt(dto.getExpiresAt());
        msg.setPoll(dto.getPoll());
        msg.setForwardedFrom(dto.getForwardedFrom());
        msg.setMentions(dto.getMentions() != null ? dto.getMentions() : new ArrayList<>());
        msg.setEditHistory(new ArrayList<>());
        msg = messageRepo.save(msg);

        updateLastMessage(msg);
        incrementUnreadForOthers(dto.getConversationId(), uid);

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + dto.getConversationId(), msg);

        return msg;
    }

    @Override
    public Message getMessageById(Long id) {
        return messageRepo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Message not found: " + id));
    }

    @Override
    public Page<Message> getMessages(Long conversationId, Pageable pageable) {
        assertMember(conversationId, UserContext.getUserId());
        return messageRepo.findByConversationIdAndDeletedAtIsNull(conversationId, pageable);
    }

    @Override
    @Transactional
    public Message editMessage(Long messageId, EditMessageDTO dto) {
        Message msg = getMessageById(messageId);
        assertOwner(msg);

        EditHistoryEntry history = new EditHistoryEntry(msg.getContent(), LocalDateTime.now());
        msg.getEditHistory().add(history);
        msg.setContent(dto.getContent());
        msg.setEditedAt(LocalDateTime.now());
        msg = messageRepo.save(msg);

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + msg.getConversationId() + "/edited", msg);

        return msg;
    }

    @Override
    @Transactional
    public void deleteMessage(Long messageId) {
        Message msg = getMessageById(messageId);
        assertOwner(msg);
        msg.softDelete();
        messageRepo.save(msg);

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + msg.getConversationId() + "/deleted", messageId);
    }

    @Override
    @Transactional
    public MessageReaction addReaction(Long messageId, AddReactionDTO dto) {
        Long uid = UserContext.getUserId();
        if (reactionRepo.existsByMessageIdAndUserIdAndEmoji(messageId, uid, dto.getEmoji())) {
            throw new AppException(HttpStatus.CONFLICT, "Reaction already added");
        }
        MessageReaction reaction = new MessageReaction();
        reaction.setMessageId(messageId);
        reaction.setUserId(uid);
        reaction.setEmoji(dto.getEmoji());
        return reactionRepo.save(reaction);
    }

    @Override
    @Transactional
    public void removeReaction(Long messageId, String emoji) {
        Long uid = UserContext.getUserId();
        if (!reactionRepo.existsByMessageIdAndUserIdAndEmoji(messageId, uid, emoji)) {
            throw new AppException(HttpStatus.NOT_FOUND, "Reaction not found");
        }
        reactionRepo.deleteByMessageIdAndUserIdAndEmoji(messageId, uid, emoji);
    }

    @Override
    public List<MessageReaction> getReactions(Long messageId) {
        return reactionRepo.findByMessageId(messageId);
    }

    @Override
    @Transactional
    public void markDelivered(Long messageId) {
        Long uid = UserContext.getUserId();
        Message msg = getMessageById(messageId);
        receiptRepo.findByMessageIdAndUserId(messageId, uid).ifPresentOrElse(
                r -> {
                    if (r.getDeliveredAt() == null) {
                        r.setDeliveredAt(LocalDateTime.now());
                        receiptRepo.save(r);
                    }
                },
                () -> {
                    MessageReceipt r = new MessageReceipt();
                    r.setMessageId(messageId);
                    r.setConversationId(msg.getConversationId());
                    r.setUserId(uid);
                    r.setDeliveredAt(LocalDateTime.now());
                    receiptRepo.save(r);
                }
        );
    }

    @Override
    @Transactional
    public void markRead(Long messageId) {
        Long uid = UserContext.getUserId();
        Message msg = getMessageById(messageId);
        receiptRepo.findByMessageIdAndUserId(messageId, uid).ifPresentOrElse(
                r -> {
                    if (r.getReadAt() == null) {
                        r.setReadAt(LocalDateTime.now());
                        receiptRepo.save(r);
                    }
                },
                () -> {
                    MessageReceipt r = new MessageReceipt();
                    r.setMessageId(messageId);
                    r.setConversationId(msg.getConversationId());
                    r.setUserId(uid);
                    r.setDeliveredAt(LocalDateTime.now());
                    r.setReadAt(LocalDateTime.now());
                    receiptRepo.save(r);
                }
        );
        conversationService.markAsRead(msg.getConversationId(), messageId);
    }

    private void updateLastMessage(Message msg) {
        conversationRepo.findById(msg.getConversationId()).ifPresent(conv -> {
            LastMessageInfo info = new LastMessageInfo(
                    msg.getId(), msg.getSenderId(), msg.getContent(), msg.getCreatedAt());
            conv.setLastMessage(info);
            conversationRepo.save(conv);
        });
    }

    private void incrementUnreadForOthers(Long conversationId, Long senderId) {
        userConvRepo.findByUserIdAndConversationId(senderId, conversationId)
                .ifPresent(uc -> { uc.setUnreadCount(0); userConvRepo.save(uc); });

        memberRepo.findByConversationId(conversationId).stream()
                .filter(m -> !m.getUserId().equals(senderId) && !m.isBlocked())
                .forEach(m -> {
                    UserConversation uc = conversationService.getOrCreateUserConversation(
                            m.getUserId(), conversationId);
                    uc.setUnreadCount(uc.getUnreadCount() + 1);
                    userConvRepo.save(uc);
                });
    }

    private void assertMember(Long conversationId, Long userId) {
        if (!memberRepo.existsByConversationIdAndUserId(conversationId, userId)) {
            throw new AppException(HttpStatus.FORBIDDEN, "Not a member of this conversation");
        }
    }

    private void assertOwner(Message msg) {
        if (!msg.getSenderId().equals(UserContext.getUserId())) {
            throw new AppException(HttpStatus.FORBIDDEN, "Not the message owner");
        }
    }
}
