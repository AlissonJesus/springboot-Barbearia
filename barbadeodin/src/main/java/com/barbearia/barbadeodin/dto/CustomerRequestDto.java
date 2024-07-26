package com.barbearia.barbadeodin.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerRequestDto(
		@NotBlank
		String name,
		@NotBlank
		String phone,
		String email) {
	
}
