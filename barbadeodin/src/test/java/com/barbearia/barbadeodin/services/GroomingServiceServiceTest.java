package com.barbearia.barbadeodin.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.barbearia.barbadeodin.dto.GroomingServiceDetailsDto;
import com.barbearia.barbadeodin.dto.GroomingServiceDto;
import com.barbearia.barbadeodin.models.GroomingService;
import com.barbearia.barbadeodin.repositories.GroomingServiceRepository;

@ExtendWith(MockitoExtension.class)
class GroomingServiceServiceTest {

	@InjectMocks
	private GroomingServiceService service;
	
	@Mock
	private GroomingServiceRepository repository;
	
	private GroomingServiceDto payload = createServiceDto();
	private GroomingService detailedServiceModel = createServiceDetailModel(1L); 
	private GroomingServiceDetailsDto detailedServiceDto = createServiceDetailsDto();
	
	@Test
	void shouldRegisterServiceSuccessfully() {
		prepareScenarioRegister();
		GroomingServiceDetailsDto detailedServiceDto = service.register(payload);
		verifyResultScenario(detailedServiceDto);
	}
	
	private GroomingServiceDetailsDto createServiceDetailsDto() {
		return new GroomingServiceDetailsDto(detailedServiceModel);
	}

	@Test
	void shouldGetByIdSuccessfully() {
		prepareScenarioGetById();
		GroomingServiceDetailsDto detailedService = service.getById(1L);
		verifyResultScenarioGetById(detailedService);
	}
	
	@Test
	void shouldGetAllSuccessfully() {
		prepareScenarioGetAll();
		List<GroomingServiceDetailsDto> detailedService = service.getAll();
		verifyResultScenarioGetAll(detailedService);
	}
	
	private void verifyResultScenarioGetAll(List<GroomingServiceDetailsDto> detailedService) {
		verifyDetailedService(detailedService.get(0));
	}

	private List<GroomingService> createGroomingServiceDetailsList() {
		return List.of(detailedServiceModel);
	}

	private void prepareScenarioGetAll() {
		when(repository.findAll()).thenReturn(createGroomingServiceDetailsList());
	}
	

	private void verifyResultScenarioGetById(GroomingServiceDetailsDto detailedService) {
		verifyDetailedService(detailedService);
		verify(repository, times(1)).getReferenceById(any(Long.class));
	}

	private void prepareScenarioGetById() {
		when(repository.getReferenceById(any(Long.class))).thenReturn(detailedServiceModel);
	}

	private void verifyResultScenario(GroomingServiceDetailsDto detailedService) {
		verifyDetailedService(detailedService);
		verify(repository, times(1)).save(any(GroomingService.class));
	}

	private void verifyDetailedService(GroomingServiceDetailsDto detailedServiceDto) {
		assertNotNull(detailedServiceDto);
		assertThat(1L).isEqualTo(detailedServiceDto.id());
		assertThat(payload.name()).isEqualTo(detailedServiceDto.name());
		assertThat(payload.description()).isEqualTo(detailedServiceDto.description());
		assertThat(payload.price()).isEqualTo(detailedServiceDto.price());
		assertThat(payload.duration()).isEqualTo(detailedServiceDto.duration());
	}

	private void prepareScenarioRegister() {
		when(repository.save(any(GroomingService.class))).thenReturn(detailedServiceModel);
	}
	
	private GroomingService createServiceDetailModel(Long id) {
		GroomingService serviceModel = new GroomingService(payload);  
		serviceModel.setId(id);
		return serviceModel;
	}
	
	
	private GroomingServiceDto createServiceDto() {
		return new GroomingServiceDto(
				"Corte", 
				"Corte de Cabelo Profissional a pedido do cliente, finalizando no lavatório com Shampoo especializado e penteado ao final!",
				25, 30);
	};
}
