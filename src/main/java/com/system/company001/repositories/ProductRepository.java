package com.system.company001.repositories;

import com.system.company001.dtos.ProductListRecordDto;
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
                    "SELECT new com.system.company001.dtos.ProductListRecordDto(" +
                    "p.idProduct, p.name, p.value, p.image, p.thumbnail) " +
                    "FROM ProductModel p ORDER BY p.name ASC",
            countQuery = "SELECT count(p) FROM ProductModel p"
    )
    Page<ProductListRecordDto> findAllProductListRecordDto(Pageable pageable);
}
