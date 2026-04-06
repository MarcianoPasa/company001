package com.system.company001.repositories;

import com.system.company001.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {

    List<ProductModel> findAllByOrderByNameAsc();

}
