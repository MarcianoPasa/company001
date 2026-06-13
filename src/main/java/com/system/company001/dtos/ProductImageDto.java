package com.system.company001.dtos;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public record ProductImageDto(
        UUID idProduct,
        byte[] image
) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductImageDto that = (ProductImageDto) o;
        return Objects.deepEquals(image, that.image) && Objects.equals(idProduct, that.idProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduct, Arrays.hashCode(image));
    }

    @Override
    public String toString() {
        return "ProductImageDto{" +
                "idProduct=" + idProduct +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}