package com.system.company001.controllers;

import com.system.company001.dtos.ProductListRecordDto;
import com.system.company001.dtos.ProductRecordDto;
import com.system.company001.models.ProductModel;
import com.system.company001.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<ProductListRecordDto>>> getAllProducts(
			Pageable pageable,
			PagedResourcesAssembler<ProductListRecordDto> assembler) {
		Page<ProductListRecordDto> productsPage = productService.getAllProducts(pageable);
		PagedModel<EntityModel<ProductListRecordDto>> pagedModel = assembler.toModel(productsPage,
				productDto -> EntityModel.of(productDto,
						linkTo(methodOn(ProductController.class).getOneProduct(productDto.idProduct())).withSelfRel()
				)
		);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getOneProduct(@PathVariable(value="id") UUID id) {
		return productService.getOneProduct(id)
				.map(product -> {
					product.add(linkTo(methodOn(ProductController.class)
							.getAllProducts(null, null)).withRel("productList"));
					return ResponseEntity.ok((Object) product);
				})
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado."));
	}

	@PostMapping
	public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
		ProductModel savedProduct = productService.save(productRecordDto);
		URI location = linkTo(methodOn(ProductController.class)
				.getOneProduct(savedProduct.getIdProduct())).toUri();
		return ResponseEntity.created(location).body(savedProduct);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductModel> updateProduct(
			@PathVariable UUID id,
			@RequestBody @Valid ProductRecordDto productRecordDto
	) {
		return productService.update(id, productRecordDto)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
		return productService.deleteProduct(id)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}
}