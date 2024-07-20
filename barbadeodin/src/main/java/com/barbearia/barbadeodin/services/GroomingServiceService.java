package com.barbearia.barbadeodin.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barbearia.barbadeodin.dto.GroomingServiceDetailsDto;
import com.barbearia.barbadeodin.dto.GroomingServiceDto;
import com.barbearia.barbadeodin.models.GroomingService;
import com.barbearia.barbadeodin.repositories.GroomingServiceRepository;

@Service
public class GroomingServiceService {
	
	@Autowired
	private GroomingServiceRepository  repository;
	public GroomingServiceDetailsDto register(GroomingServiceDto payload) {
		GroomingService service = new GroomingService(payload);;
		return new GroomingServiceDetailsDto(repository.save(service));
	}
	

	public GroomingServiceDetailsDto getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<GroomingServiceDetailsDto> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public GroomingServiceDetailsDto updateById(Long id, GroomingServiceDto service) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

}