package com.system.company001.dtos;

import com.system.company001.models.CustomerModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;

public record CustomerRecordDto(
        @NotBlank String corporateName,
        @NotNull String businessName,
        @NotNull String businessTaxId
) {
    public CustomerModel convertToCustomerModel() {
        var customerModel = new CustomerModel();
        BeanUtils.copyProperties(this, customerModel);
        return customerModel;
    }
}
