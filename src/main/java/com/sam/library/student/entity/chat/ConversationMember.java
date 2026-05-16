package com.sam.library.student.entity.chat;

import com.sam.library.student.entity.BaseEntity;
import com.sam.library.student.enums.MemberRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"conversation_id", "user_id"}))
@Data
@EqualsAndHashCode(callSuper = true)
public class ConversationMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_id", nullable = false)
    private Long conversationId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private MemberRole role = MemberRole.MEMBER;

    @Column(name = "is_blocked")
    private boolean blocked = false;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
}
