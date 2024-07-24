package com.barbearia.barbadeodin.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barbearia.barbadeodin.dto.GroomingServiceDetailsDto;
import com.barbearia.barbadeodin.dto.SpecialistDetailDto;
import com.barbearia.barbadeodin.dto.SpecialistDto;
import com.barbearia.barbadeodin.models.GroomingService;
import com.barbearia.barbadeodin.models.Specialist;
import com.barbearia.barbadeodin.repositories.GroomingServiceRepository;
import com.barbearia.barbadeodin.repositories.ServiceIdOnly;
import com.barbearia.barbadeodin.repositories.SpecialistProjection;
import com.barbearia.barbadeodin.repositories.SpecialistRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class SpecialistService {

	@Autowired
	private SpecialistRepository repository;
	@Autowired
	private GroomingServiceRepository serviceRepository;

	@Transactional
    public SpecialistDetailDto register(SpecialistDto requestDto) {
        List<GroomingService> services = serviceRepository.findAllById(requestDto.serviceIds());
        Specialist specialist = createSpecialist(requestDto, services);
        Specialist savedSpecialist = repository.save(specialist);
        List<Long> serviceIds = savedSpecialist.getServices().stream()
                .map(GroomingService::getId)
                .collect(Collectors.toList());
        return new SpecialistDetailDto(
                savedSpecialist.getId(),
                savedSpecialist.getName(),
                savedSpecialist.getImagemUrl(),
                serviceIds
        );
    }
	
	private Specialist createSpecialist(SpecialistDto requestDto, List<GroomingService> services) {
		return new Specialist(requestDto, services);
	}

	public List<SpecialistDetailDto> getAll() {
        return repository.findAllWithServiceIds().stream()
                .map(projection -> new SpecialistDetailDto(
                        projection.getId(),
                        projection.getName(),
                        projection.getImagemUrl(),
                        projection.getServices().stream()
                                .map(ServiceIdOnly::getId)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

	public SpecialistDetailDto getById(Long id) {
		var specialistFound = repository.findSpecialistById(id)
                .map(projection -> new SpecialistDetailDto(
                        projection.getId(),
                        projection.getName(),
                        projection.getImagemUrl(),
                        projection.getServices().stream()
                                .map(ServiceIdOnly::getId)
                                .collect(Collectors.toList())
                ));
		
        return specialistFound.get();
    }


	@Transactional
	public SpecialistDetailDto updateById(Long id, SpecialistDto requestDto ) {
		Specialist specialist = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Specialist not found with id " + id));

        List<GroomingService> services = serviceRepository.findAllById(requestDto.serviceIds());
        updateSpecialistFields(specialist, requestDto, services);

        Specialist updatedSpecialist = repository.save(specialist);

        List<Long> serviceIds = updatedSpecialist.getServices().stream()
                .map(GroomingService::getId)
                .collect(Collectors.toList());

        return new SpecialistDetailDto(
                updatedSpecialist.getId(),
                updatedSpecialist.getName(),
                updatedSpecialist.getImagemUrl(),
                serviceIds
        );
	}
	
	
	private void updateSpecialistFields(Specialist specialist, SpecialistDto requestDto, List<GroomingService> services) {
        specialist.setName(requestDto.name());
        specialist.setImagemUrl(requestDto.imagemUrl());
        specialist.setServices(services);
    }
	
	

	

	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	private List<GroomingServiceDetailsDto> createGroomingServiceDetailsDto(List<GroomingService> services) {
		return services.stream().map(GroomingServiceDetailsDto::new).toList();
	}
	
	

}
