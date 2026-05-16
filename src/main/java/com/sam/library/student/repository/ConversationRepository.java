package com.sam.library.student.repository;

import com.sam.library.student.entity.chat.Conversation;
import com.sam.library.student.enums.ConversationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByInviteToken(String inviteToken);

    @Query("""
            SELECT c FROM Conversation c
            JOIN ConversationMember m ON m.conversationId = c.id
            WHERE m.userId = :userId AND m.blocked = false
            ORDER BY c.updatedAt DESC
            """)
    Page<Conversation> findByMemberUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
            SELECT c FROM Conversation c
            JOIN ConversationMember m1 ON m1.conversationId = c.id AND m1.userId = :userId1
            JOIN ConversationMember m2 ON m2.conversationId = c.id AND m2.userId = :userId2
            WHERE c.type = 'PRIVATE'
            """)
    Optional<Conversation> findPrivateConversation(
            @Param("userId1") Long userId1,
            @Param("userId2") Long userId2);

    List<Conversation> findByType(ConversationType type);
}
