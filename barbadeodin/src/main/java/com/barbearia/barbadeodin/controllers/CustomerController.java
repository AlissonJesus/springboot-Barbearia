package com.barbearia.barbadeodin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
}
