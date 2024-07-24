package com.barbearia.barbadeodin.dto;

import java.util.List;

import com.barbearia.barbadeodin.models.Specialist;

public record SpecialistDetailDto(
	    Long id,
	    String name,
	    String imagemUrl,
	    List<Long> serviceIds
	    
	    
	) {

	public SpecialistDetailDto(Specialist specialist, List<Long> serviceIds) {
		this(
				specialist.getId(),
				specialist.getName(),
				specialist.getImagemUrl(),
				serviceIds);
	}}