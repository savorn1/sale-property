package com.sam.library.student.dto;

import lombok.Data;

/**
 * DTO that carries all editable system settings as typed fields.
 * Add new settings here as the product grows.
 */
@Data
public class SystemSettingDTO {

    /**
     * When {@code true} an order can be placed even when a product has
     * zero or negative stock (overselling / back-order mode).
     * When {@code false} (default) placing an order with insufficient
     * stock throws a 400 Bad Request.
     */
    private boolean allowOverselling;
}
