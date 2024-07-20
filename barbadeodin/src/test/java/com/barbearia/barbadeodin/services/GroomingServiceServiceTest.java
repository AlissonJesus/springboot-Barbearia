package com.barbearia.barbadeodin.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
	
	@Test
	void shouldRegisterServiceSuccessfully() {
		prepareScenarioRegister();
		GroomingServiceDetailsDto detailedServiceDto = service.register(payload);
		verifyResultScenario(detailedServiceDto);
	}

	private void verifyResultScenario(GroomingServiceDetailsDto detailedServiceDto) {
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
