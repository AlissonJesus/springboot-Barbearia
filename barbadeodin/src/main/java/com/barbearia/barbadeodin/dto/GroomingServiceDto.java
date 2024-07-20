package com.barbearia.barbadeodin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GroomingServiceDto(
		
		@NotBlank
		String name, 
		String description, 
		
		@NotNull
		double price, 
		@NotNull
		Integer duration) {
	
}
