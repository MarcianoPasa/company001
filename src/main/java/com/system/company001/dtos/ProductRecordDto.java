package com.system.company001.dtos;

import com.system.company001.models.ProductModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRecordDto(
        @NotBlank String name,
        @NotNull BigDecimal value,
        String image
) {
    public ProductModel convertToProductModel() {
        var product = new ProductModel();
        product.setName(this.name());
        product.setValue(this.value());
        return product;
    }
}