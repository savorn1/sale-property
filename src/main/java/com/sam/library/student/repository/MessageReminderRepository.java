package com.sam.library.student.repository;

import com.sam.library.student.entity.chat.MessageReminder;
import com.sam.library.student.enums.ReminderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageReminderRepository extends JpaRepository<MessageReminder, Long> {

    List<MessageReminder> findByUserIdAndStatus(Long userId, ReminderStatus status);

    List<MessageReminder> findByRemindAtBeforeAndStatus(LocalDateTime time, ReminderStatus status);
}
