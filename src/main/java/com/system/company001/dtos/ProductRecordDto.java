package com.system.company001.dtos;

import com.system.company001.models.ProductModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Base64;

public record ProductRecordDto(@NotBlank String name, @NotNull BigDecimal value, String image) {

    public ProductModel convertToProductModel(){
        var productModel = new ProductModel();
        BeanUtils.copyProperties(this, productModel, "image");
        if (this.image != null && this.image.contains(",")) {
            String base64Image = this.image.split(",")[1];
            productModel.setImage(Base64.getDecoder().decode(base64Image));
        }
        return productModel;
    }
}
