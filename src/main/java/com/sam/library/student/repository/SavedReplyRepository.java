package com.sam.library.student.repository;

import com.sam.library.student.entity.chat.SavedReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedReplyRepository extends JpaRepository<SavedReply, Long> {

    List<SavedReply> findByUserId(Long userId);

    Optional<SavedReply> findByUserIdAndShortcut(Long userId, String shortcut);

    boolean existsByUserIdAndShortcut(Long userId, String shortcut);
}
