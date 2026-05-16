package com.sam.library.student.repository;

import com.sam.library.student.entity.chat.UserConversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserConversationRepository extends JpaRepository<UserConversation, Long> {

    Optional<UserConversation> findByUserIdAndConversationId(Long userId, Long conversationId);

    List<UserConversation> findByUserId(Long userId);

    List<UserConversation> findByUserIdAndArchived(Long userId, boolean archived);

    List<UserConversation> findByUserIdAndMuted(Long userId, boolean muted);
}
