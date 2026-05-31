package com.system.company001.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductListResponseDto(
        UUID idProduct,
        String name,
        BigDecimal value
) {}