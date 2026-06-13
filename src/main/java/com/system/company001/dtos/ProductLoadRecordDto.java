package com.system.company001.dtos;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public record ProductLoadRecordDto(
        UUID idProduct,
        String name,
        BigDecimal value,
        byte[] thumbnail
) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductLoadRecordDto that = (ProductLoadRecordDto) o;
        return Objects.equals(name, that.name) && Objects.equals(idProduct, that.idProduct) &&
                Objects.equals(value, that.value) && Objects.deepEquals(thumbnail, that.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduct, name, value, Arrays.hashCode(thumbnail));
    }

    @Override
    public String toString() {
        return "ProductLoadRecordDto{" +
                "idProduct=" + idProduct +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", thumbnail=" + Arrays.toString(thumbnail) +
                '}';
    }
}
