package com.sam.library.student.repository;

import com.sam.library.student.entity.chat.ScheduledMessage;
import com.sam.library.student.enums.ScheduledMessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledMessageRepository extends JpaRepository<ScheduledMessage, Long> {

    List<ScheduledMessage> findBySenderIdAndStatus(Long senderId, ScheduledMessageStatus status);

    List<ScheduledMessage> findByScheduledForBeforeAndStatus(LocalDateTime time, ScheduledMessageStatus status);

    List<ScheduledMessage> findByConversationIdAndStatus(Long conversationId, ScheduledMessageStatus status);
}
