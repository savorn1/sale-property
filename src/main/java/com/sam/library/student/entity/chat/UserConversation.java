package com.sam.library.student.entity.chat;

import com.sam.library.student.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_conversations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "conversation_id"}))
@Data
@EqualsAndHashCode(callSuper = true)
public class UserConversation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "conversation_id", nullable = false)
    private Long conversationId;

    @Column(name = "last_read_message_id")
    private Long lastReadMessageId;

    @Column(name = "unread_count")
    private int unreadCount = 0;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    private boolean muted = false;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "starred_message_ids", columnDefinition = "jsonb")
    private List<Long> starredMessageIds = new ArrayList<>();

    private boolean archived = false;
}
