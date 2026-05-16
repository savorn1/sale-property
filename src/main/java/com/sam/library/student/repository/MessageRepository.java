package com.sam.library.student.repository;

import com.sam.library.student.entity.chat.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByConversationIdAndDeletedAtIsNull(Long conversationId, Pageable pageable);

    List<Message> findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long conversationId);

    @Query(value = """
            SELECT * FROM messages
            WHERE conversation_id = :cid
              AND deleted_at IS NULL
              AND mentions @> CAST(CONCAT('[', :userId, ']') AS jsonb)
            """, nativeQuery = true)
    List<Message> findMentions(@Param("cid") Long conversationId, @Param("userId") Long userId);

    long countByConversationIdAndDeletedAtIsNull(Long conversationId);
}
