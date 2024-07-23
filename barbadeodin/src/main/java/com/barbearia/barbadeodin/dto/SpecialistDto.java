package com.barbearia.barbadeodin.dto;

import java.util.List;

public record SpecialistDto(
		String name, 
		String imagemUrl,
		List<Long> serviceIds) {
}
