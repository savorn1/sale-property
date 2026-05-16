package com.sam.library.student.entity.chat;

import com.sam.library.student.entity.BaseEntity;
import com.sam.library.student.enums.ReminderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "message_reminders")
@Data
@EqualsAndHashCode(callSuper = true)
public class MessageReminder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "conversation_id")
    private Long conversationId;

    @Column(name = "remind_at", nullable = false)
    private LocalDateTime remindAt;

    private String note;

    @Column(name = "message_content", columnDefinition = "text")
    private String messageContent;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private ReminderStatus status = ReminderStatus.PENDING;

    @Column(name = "job_id")
    private String jobId;
}
