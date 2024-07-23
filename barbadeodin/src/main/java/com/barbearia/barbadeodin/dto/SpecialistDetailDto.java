package com.barbearia.barbadeodin.dto;

import java.util.List;

import com.barbearia.barbadeodin.models.GroomingService;
import com.barbearia.barbadeodin.models.Specialist;

public record SpecialistDetailDto(Long id, String name, String imageUrl, List<GroomingServiceDetailsDto> services) {

	public SpecialistDetailDto(Specialist specialist, List<GroomingServiceDetailsDto> services) {
		this(specialist.getId(), 
	             specialist.getName(), 
	             specialist.getImagemUrl(), 
	             services);
	}

	

}
