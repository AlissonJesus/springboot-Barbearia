package com.barbearia.barbadeodin.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.barbearia.barbadeodin.dto.ServiceDetailsDto;
import com.barbearia.barbadeodin.dto.ServiceDto;
import com.barbearia.barbadeodin.models.Service;
import com.barbearia.barbadeodin.repositories.ServiceRepository;

@ExtendWith(MockitoExtension.class)
class ServiceServiceTest {

	@Autowired
	private ServiceService service;
	
	@Mock
	private ServiceRepository repository;
	
	private ServiceDto payload = createServiceDto();
	private Service detailedService = createServiceDetailModel(1L); 
	
	@Test
	void shouldRegisterServiceSuccessfully() {
		prepareScenarioRegister();
		ServiceDetailsDto detailedServiceDto = service.register(payload);
		verifyResultScenario(detailedServiceDto);
	}

	private void verifyResultScenario(ServiceDetailsDto detailedServiceDto) {
		assertNotNull(detailedServiceDto);
		assertThat(1L).isEqualTo(detailedServiceDto.id());
		assertThat(payload.name()).isEqualTo(detailedServiceDto.name());
		assertThat(payload.description()).isEqualTo(detailedServiceDto.description());
		assertThat(payload.price()).isEqualTo(detailedServiceDto.price());
		assertThat(payload.duration()).isEqualTo(detailedServiceDto.duration());
	}

	private void prepareScenarioRegister() {
		when(repository.save(any(Service.class))).thenReturn(detailedService);
	}
	
	private Service createServiceDetailModel(Long id) {
		Service serviceModel = new Service(payload);  
		return serviceModel;
	}
	
	
	private ServiceDto createServiceDto() {
		return new ServiceDto(
				"Corte", 
				"Corte de Cabelo Profissional a pedido do cliente, finalizando no lavatório com Shampoo especializado e penteado ao final!",
				25, 30);
	};
}
