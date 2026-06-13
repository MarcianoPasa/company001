package com.system.company001.controllers;

import com.system.company001.dtos.*;
import com.system.company001.services.ProductService;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {

	private final ProductService productService;
	private final PagedResourcesAssembler<ProductListResponseDto> assembler;

	public ProductController(ProductService productService, PagedResourcesAssembler<ProductListResponseDto> assembler) {
		this.productService = productService;
        this.assembler = assembler;
    }

	@GetMapping("/products")
	public ResponseEntity<PagedModel<EntityModel<ProductListResponseDto>>> getAllProducts(
			Pageable pageable,
			@NonNull PagedResourcesAssembler<ProductListResponseDto> assembler
	) {
		Page<ProductListResponseDto> productsPage = productService.getAllProducts(pageable);

		PagedModel<EntityModel<ProductListResponseDto>> pagedModel = assembler.toModel(productsPage,
				productDto -> EntityModel.of(productDto,
						linkTo(methodOn(ProductController.class).getOneProduct(productDto.idProduct())).withSelfRel()
				)
		);

		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<EntityModel<ProductLoadRecordDto>> getOneProduct(@PathVariable UUID id) {
		return productService.getOneProduct(id)
				.map(dto -> {
					EntityModel<ProductLoadRecordDto> entityModel = EntityModel.of(dto,
							linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel(),
							linkTo(methodOn(ProductController.class).getAllProducts(
									null, assembler)).withRel("productsLoad")
					);
					return ResponseEntity.ok(entityModel);
				})
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/products")
	public ResponseEntity<ProductRecordDto> saveProduct(
			@RequestBody @Valid ProductImageRecordDto productImageRecordDto
	) {
		ProductRecordDto savedProductDto = productService.save(productImageRecordDto);
		URI location = linkTo(methodOn(ProductController.class)
				.getOneProduct(savedProductDto.idProduct())).toUri();
		return ResponseEntity.created(location).body(savedProductDto);
	}

	@PutMapping("/products/{id}")
	public ResponseEntity<ProductRecordDto> updateProduct(
			@PathVariable UUID id,
			@RequestBody @Valid ProductImageRecordDto productImageRecordDto
	) {
		return productService.update(id, productImageRecordDto)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
		return productService.deleteProduct(id)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}

    @GetMapping("/products/image/{id}")
    public ResponseEntity<ProductImageDto> getImageFullByIdProduct(@PathVariable UUID id) {
        return productService.getImageFullByIdProduct(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}