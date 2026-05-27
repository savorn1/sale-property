package com.sam.library.student.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Single-row-per-key system settings table.
 * Keys are stable identifiers (e.g. "allowOverselling").
 */
@Entity
@Table(name = "system_setting")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemSetting {

    @Id
    @Column(length = 100)
    private String key;

    @Column(nullable = false, columnDefinition = "text")
    private String value;
}
