package com.system.company001.repositories;

import com.system.company001.dtos.ProductImageDto;
import com.system.company001.dtos.ProductListResponseDto;
import com.system.company001.dtos.ProductLoadRecordDto;
import com.system.company001.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {

    @Query(
            value =
                "SELECT new com.system.company001.dtos.ProductListResponseDto(" +
                "p.idProduct, p.name, p.value, p.image, p.thumbnail) " +
                "FROM ProductModel p ORDER BY p.name ASC"
    )
    Page<ProductListResponseDto> findAllProductsListRecordDto(Pageable pageable);

    @Query(
            value =
                "SELECT new com.system.company001.dtos.ProductLoadRecordDto(" +
                "p.idProduct, p.name, p.value, p.thumbnail) " +
                "FROM ProductModel p WHERE p.idProduct = :idProduct"
    )
    Optional <ProductLoadRecordDto> findProductByIdRecordDto(@Param("idProduct") UUID idProduct);

    @Query(
            value =
                "SELECT new com.system.company001.dtos.ProductImageDto(p.idProduct, p.image) " +
                "FROM ProductModel p " +
                "WHERE p.idProduct = :idProduct"
    )
    Optional<ProductImageDto> findImageByIdProduct(@Param("idProduct") UUID idProduct);
}