package com.sam.library.student.entity.chat;

import com.sam.library.student.entity.BaseEntity;
import com.sam.library.student.enums.MessageType;
import com.sam.library.student.enums.ScheduledMessageStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_messages")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduledMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_id", nullable = false)
    private Long conversationId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(columnDefinition = "text")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageType type = MessageType.TEXT;

    @Column(name = "reply_to")
    private Long replyTo;

    @Column(name = "scheduled_for", nullable = false)
    private LocalDateTime scheduledFor;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private ScheduledMessageStatus status = ScheduledMessageStatus.PENDING;

    @Column(name = "job_id")
    private String jobId;
}
