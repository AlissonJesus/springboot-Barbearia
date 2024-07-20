package com.barbearia.barbadeodin.dto;

import com.barbearia.barbadeodin.models.GroomingService;

public record GroomingServiceDetailsDto(Long id, String name, String description, double price, int duration) {

	public GroomingServiceDetailsDto(GroomingService service) {
		this(service.getId(), service.getName(), service.getDescription(), service.getPrice(), service.getDuration());
	}

}
