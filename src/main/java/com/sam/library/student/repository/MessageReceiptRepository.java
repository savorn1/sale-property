package com.sam.library.student.repository;

import com.sam.library.student.entity.chat.MessageReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageReceiptRepository extends JpaRepository<MessageReceipt, Long> {

    List<MessageReceipt> findByMessageId(Long messageId);

    Optional<MessageReceipt> findByMessageIdAndUserId(Long messageId, Long userId);

    List<MessageReceipt> findByConversationIdAndUserId(Long conversationId, Long userId);

    List<MessageReceipt> findByConversationIdAndUserIdAndReadAtIsNull(Long conversationId, Long userId);
}
