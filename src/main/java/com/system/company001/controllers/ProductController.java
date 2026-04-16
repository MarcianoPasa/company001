package com.system.company001.controllers;

import com.system.company001.dtos.ProductRecordDto;
import com.system.company001.models.ProductModel;
import com.system.company001.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {

	final ProductRepository productRepository;

	private PagedResourcesAssembler<@NonNull ProductModel> pagedResourcesAssembler;

	public ProductController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@GetMapping("/products")
	public ResponseEntity<PagedModel<EntityModel<ProductModel>>> getAllProducts(
			Pageable pageable, PagedResourcesAssembler<ProductModel> assembler
	) {
//		Page<ProductModel> productsPage = productRepository.findAllByOrderByNameAsc(pageable);
//		PagedModel<EntityModel<ProductModel>> pagedModel = assembler.toModel(productsPage, product -> {
//			return EntityModel.of(product,
//					linkTo(methodOn(ProductController.class).getOneProduct(product.getIdProduct())).withSelfRel());
//		});

		Page<ProductModel> productsPage = productRepository.findAllByOrderByNameAsc(pageable);
		PagedModel<EntityModel<ProductModel>> pagedModel =
				assembler.toModel(productsPage, ProductController::toModel);
		
		return ResponseEntity.ok(pagedModel);
	}

	private static EntityModel<ProductModel> toModel(ProductModel product) {
		return EntityModel.of(product,
				linkTo(methodOn(ProductController.class).getOneProduct(product.getIdProduct())).withSelfRel());
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<Object> getOneProduct(@PathVariable(value="id") UUID id){
		return productRepository.findById(id)
				.map(product -> {
					product.add(linkTo(methodOn(ProductController.class).getAllProducts(
							null, pagedResourcesAssembler))
							.withRel("Products List"));
					return ResponseEntity.status(HttpStatus.OK).body((Object) product);
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found."));
	}

	@PostMapping("/products")
	public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(productRepository.save(productRecordDto.convertToProductModel()));
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<Object> deleteProduct(@PathVariable(value="id") UUID id) {
		return productRepository.findById(id)
				.map(product -> {
					productRepository.delete(product);
					return ResponseEntity.status(HttpStatus.OK).body((Object) "Product deleted successfully.");
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found."));
	}
	
	@PutMapping("/products/{id}")
	public ResponseEntity<Object> updateProduct(
			@PathVariable(value="id") UUID id,
			@RequestBody @Valid ProductRecordDto productRecordDto
	) {
		return productRepository.findById(id)
				.map(product -> {
					BeanUtils.copyProperties(productRecordDto, product);
					return ResponseEntity.status(HttpStatus.OK).body((Object) productRepository.save(product));
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found."));
	}
}
