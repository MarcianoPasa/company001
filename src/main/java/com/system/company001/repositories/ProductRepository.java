package com.system.company001.repositories;

import com.system.company001.dtos.ProductListResponseDto;
import com.system.company001.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {

    @Query(
            value =
                    "SELECT new com.system.company001.dtos.ProductListResponseDto(" +
                    "p.idProduct, p.name, p.value) " +
                    "FROM ProductModel p"
    )
    Page<ProductListResponseDto> findAllProductsListRecordDto(Pageable pageable);
}