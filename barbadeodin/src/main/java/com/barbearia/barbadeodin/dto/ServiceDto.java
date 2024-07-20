package com.barbearia.barbadeodin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServiceDto(
		
		@NotBlank
		String name, 
		String description, 
		
		@NotNull
		double price, 
		@NotNull
		Integer duration) {
	
}
