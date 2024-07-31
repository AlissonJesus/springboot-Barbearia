package com.barbearia.barbadeodin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.barbadeodin.dto.AppointmentRequestDto;
import com.barbearia.barbadeodin.dto.AppointmentResponseDto;
import com.barbearia.barbadeodin.services.AppointmentService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("appointments")
public class AppointmentController {
	@Autowired
	private AppointmentService service;
	
	@PostMapping
	@Transactional
	public ResponseEntity<AppointmentResponseDto> register(@RequestBody AppointmentRequestDto body) {
		var registeredAppointment = service.register(body);
		return ResponseEntity.created(null).body(registeredAppointment);
	}
}
