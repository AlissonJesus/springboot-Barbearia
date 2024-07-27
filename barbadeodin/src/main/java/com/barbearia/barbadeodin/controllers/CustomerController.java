package com.barbearia.barbadeodin.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.barbadeodin.dto.CustomerRequestDto;
import com.barbearia.barbadeodin.dto.CustomerResponseDto;
import com.barbearia.barbadeodin.services.CustomerService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("customers")
public class CustomerController {
	
	@Autowired
	private CustomerService service;
	
	@PostMapping
	@Transactional
	public ResponseEntity<CustomerResponseDto> register(@RequestBody @Valid CustomerRequestDto requesBodyDto){
		CustomerResponseDto registeredCustomer = service.register(requesBodyDto);
		return ResponseEntity.created(null).body(registeredCustomer);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CustomerResponseDto> getByid(@PathVariable Long id){
		var existingCustomer = service.getById(id);
		return ResponseEntity.ok(existingCustomer);
	}
	
	@GetMapping
	public ResponseEntity<List<CustomerResponseDto>> getAll(){
		var existingCustomers = service.getAll();
		return ResponseEntity.ok(existingCustomers);
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<CustomerResponseDto> updateById(@RequestBody @Valid CustomerRequestDto requesBodyDto, @PathVariable Long id){
		CustomerResponseDto updatedCustomer = service.updateById(id, requesBodyDto);
		return ResponseEntity.ok(updatedCustomer);
	}
}
