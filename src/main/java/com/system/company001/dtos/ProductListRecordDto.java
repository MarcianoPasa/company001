package com.system.company001.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductListRecordDto(
        UUID idProduct,
        String name,
        BigDecimal value,
        byte[] thumbnail
) {
    public ProductListRecordDto {
        thumbnail = thumbnail != null ? thumbnail.clone() : null;
    }

    @Override
    public byte[] thumbnail() {
        return thumbnail != null ? thumbnail.clone() : null;
    }

    public ProductListRecordDto(UUID idProduct, String name, BigDecimal value, byte[] image, byte[] thumbnail) {
        this(idProduct, name, value, thumbnail);
    }
}