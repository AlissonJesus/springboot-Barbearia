package com.barbearia.barbadeodin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.barbadeodin.dto.SpecialistDetailDto;
import com.barbearia.barbadeodin.dto.SpecialistDto;
import com.barbearia.barbadeodin.services.SpecialistService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("specialists")
public class SpecialistController {
	
	@Autowired
	private SpecialistService service;
	
	@PostMapping
	public ResponseEntity<SpecialistDetailDto> register(@RequestBody @Valid SpecialistDto payload) {
		var registeredSpecialist = service.register(payload);
		return ResponseEntity.created(null).body(registeredSpecialist);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SpecialistDetailDto> obterById(@PathVariable Long id) {
		var existingSpecialist = service.getById(id);
		return ResponseEntity.ok(existingSpecialist);
	}
}
