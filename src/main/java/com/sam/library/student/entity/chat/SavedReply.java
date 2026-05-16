package com.sam.library.student.entity.chat;

import com.sam.library.student.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "saved_replies",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "shortcut"}))
@Data
@EqualsAndHashCode(callSuper = true)
public class SavedReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 50)
    private String shortcut;

    @Column(nullable = false, columnDefinition = "text")
    private String content;
}
