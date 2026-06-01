package com.system.company001.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductImageRecordDto(
        @NotBlank String name,
        @NotNull BigDecimal value,
        String image
) { }