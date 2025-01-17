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
		GroomingService service = repository.getReferenceById(id);
		return new GroomingServiceDetailsDto(service);
	}

	public List<GroomingServiceDetailsDto> getAll() {
		List<GroomingService> serviceList = repository.findAll();
		return serviceList.stream().map(GroomingServiceDetailsDto::new).toList();
	}

	public GroomingServiceDetailsDto updateById(Long id, GroomingServiceDto service) {
		GroomingService serviceFound = repository.getReferenceById(id);
		serviceFound.update(service);
		return new GroomingServiceDetailsDto(serviceFound);
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
	}

}
