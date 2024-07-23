package com.barbearia.barbadeodin.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barbearia.barbadeodin.dto.GroomingServiceDetailsDto;
import com.barbearia.barbadeodin.dto.SpecialistDetailDto;
import com.barbearia.barbadeodin.dto.SpecialistDto;
import com.barbearia.barbadeodin.models.GroomingService;
import com.barbearia.barbadeodin.models.Specialist;
import com.barbearia.barbadeodin.repositories.GroomingServiceRepository;
import com.barbearia.barbadeodin.repositories.SpecialistRepository;

@Service
public class SpecialistService {

	@Autowired
	private SpecialistRepository repository;
	@Autowired
	private GroomingServiceRepository serviceRepository;

	public SpecialistDetailDto register(SpecialistDto payload) {	
		var specialist = createSpecialist(payload);
		var serviceList = createGroomingServiceDetailsDto(specialist.getServices());
		return new SpecialistDetailDto(repository.save(specialist), serviceList);
	}

	public SpecialistDetailDto getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SpecialistDetailDto> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public SpecialistDetailDto updateById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}
	
	private List<GroomingServiceDetailsDto> createGroomingServiceDetailsDto(List<GroomingService> services) {
		return services.stream().map(GroomingServiceDetailsDto::new).toList();
	}
	
	public Specialist createSpecialist(SpecialistDto payload) {
        List<GroomingService> services = serviceRepository.findAllById(payload.serviceIds());
        return new Specialist(payload, services);
    }

}
