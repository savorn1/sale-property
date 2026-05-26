package com.sam.library.student.entity;

import com.sam.library.student.enums.OpenItemStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "open_items")
@Data
@EqualsAndHashCode(callSuper = true)
public class OpenItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "open_item_no", length = 50, nullable = false, unique = true)
    private String openItemNo;

    @Column(name = "item_date", nullable = false)
    private LocalDate itemDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OpenItemStatus status = OpenItemStatus.DRAFT;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @OneToMany(mappedBy = "openItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpenItemDetail> details = new ArrayList<>();
}
