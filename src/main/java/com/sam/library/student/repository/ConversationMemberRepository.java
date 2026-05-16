package com.sam.library.student.repository;

import com.sam.library.student.entity.chat.ConversationMember;
import com.sam.library.student.enums.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Long> {

    List<ConversationMember> findByConversationId(Long conversationId);

    List<ConversationMember> findByUserId(Long userId);

    Optional<ConversationMember> findByConversationIdAndUserId(Long conversationId, Long userId);

    boolean existsByConversationIdAndUserId(Long conversationId, Long userId);

    List<ConversationMember> findByConversationIdAndRole(Long conversationId, MemberRole role);

    List<ConversationMember> findByConversationIdAndBlocked(Long conversationId, boolean blocked);

    void deleteByConversationIdAndUserId(Long conversationId, Long userId);
}
