package com.system.company001.controllers;

import com.system.company001.dtos.CustomerRecordDto;
import com.system.company001.models.CustomerModel;
import com.system.company001.repositories.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
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
public class CustomerController {

	final CustomerRepository customerRepository;

	private PagedResourcesAssembler<CustomerModel> pagedResourcesAssembler;

	public CustomerController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@GetMapping("/customers")
	public ResponseEntity<PagedModel<EntityModel<CustomerModel>>> getAllCustomers(
			Pageable pageable, PagedResourcesAssembler<CustomerModel> assembler
	) {
//		Page<CustomerModel> customersPage = customerRepository.findAllByOrderByBusinessNameAsc(pageable);
//		PagedModel<EntityModel<CustomerModel>> pagedModel =
//				assembler.toModel(customersPage, CustomerController::toModel);
		return ResponseEntity.ok(assembler.toModel(
				customerRepository.findAllByOrderByBusinessNameAsc(pageable),
				CustomerController::toModel)
		);
	}

	private static EntityModel<CustomerModel> toModel(CustomerModel customer) {
		return EntityModel.of(customer,
				linkTo(methodOn(CustomerController.class).getOneCustomer(customer.getIdCustomer())).withSelfRel());
	}

	@GetMapping("/customers/{id}")
	public ResponseEntity<Object> getOneCustomer(@PathVariable(value="id") UUID id){
		return customerRepository.findById(id)
				.map(customer -> {
					customer.add(linkTo(methodOn(CustomerController.class).getAllCustomers(
							null, pagedResourcesAssembler))
							.withRel("Customers List"));
					return ResponseEntity.status(HttpStatus.OK).body((Object) customer);
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found."));
	}

	@PostMapping("/customers")
	public ResponseEntity<CustomerModel> saveCustomer(@RequestBody @Valid CustomerRecordDto customerRecordDto) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(customerRepository.save(customerRecordDto.convertToCustomerModel()));
	}

	@DeleteMapping("/customers/{id}")
	public ResponseEntity<Object> deleteCustomer(@PathVariable(value="id") UUID id) {
		return customerRepository.findById(id)
				.map(customer -> {
					customerRepository.delete(customer);
					return ResponseEntity.status(HttpStatus.OK).body((Object) "Customer deleted successfully.");
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found."));
	}

	@PutMapping("/customers/{id}")
	public ResponseEntity<Object> updateCustomer(
			@PathVariable(value="id") UUID id,
			@RequestBody @Valid CustomerRecordDto customerRecordDto
	) {
		return customerRepository.findById(id)
				.map(customer -> {
					BeanUtils.copyProperties(customerRecordDto, customer);
					return ResponseEntity.status(HttpStatus.OK).body((Object) customerRepository.save(customer));
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found."));
	}
}
