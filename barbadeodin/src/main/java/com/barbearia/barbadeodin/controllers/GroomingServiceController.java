package com.barbearia.barbadeodin.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.barbadeodin.dto.GroomingServiceDetailsDto;
import com.barbearia.barbadeodin.dto.GroomingServiceDto;
import com.barbearia.barbadeodin.services.GroomingServiceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("services")
public class GroomingServiceController {
	
	@Autowired
	private GroomingServiceService service;

	@PostMapping
	public ResponseEntity<GroomingServiceDetailsDto> register(@RequestBody @Valid GroomingServiceDto payload) {
		var serviceDetalhado = service.register(payload);
		return ResponseEntity.created(null).body(serviceDetalhado);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<GroomingServiceDetailsDto> getById(@PathVariable Long id) {
		var serviceRegistered = service.getById(id);
		return ResponseEntity.ok(serviceRegistered);
	}
	
	@GetMapping
	public ResponseEntity<List<GroomingServiceDetailsDto>> getAll(){
		var serviceDetailsList = service.getAll();
		return ResponseEntity.ok(serviceDetailsList);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<GroomingServiceDetailsDto> updateById(@RequestBody @Valid GroomingServiceDto payload, @PathVariable Long id) {
		GroomingServiceDetailsDto updatedService = service.updateById(id, payload);
		return ResponseEntity.ok(updatedService);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id){
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
}
