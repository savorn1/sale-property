package com.sam.library.student.entity.chat;

import com.sam.library.student.entity.BaseEntity;
import com.sam.library.student.enums.ConversationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversations")
@Data
@EqualsAndHashCode(callSuper = true)
public class Conversation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ConversationType type;

    private String name;
    private String avatar;

    @Column(name = "created_by")
    private Long createdBy;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "last_message", columnDefinition = "jsonb")
    private LastMessageInfo lastMessage;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "pinned_messages", columnDefinition = "jsonb")
    private List<PinnedMessage> pinnedMessages = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "disappearing_messages", columnDefinition = "jsonb")
    private DisappearingSettings disappearingMessages;

    @Column(name = "invite_token", unique = true)
    private String inviteToken;
}
