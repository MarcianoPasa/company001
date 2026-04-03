package com.system.company001.dtos;

import com.system.company001.models.ProductModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

public record ProductRecordDto(@NotBlank String name, @NotNull BigDecimal value) {

    public ProductModel convertToProductModel(){
        var productModel = new ProductModel();
        BeanUtils.copyProperties(this, productModel);
        return productModel;
    }
}
