package com.barbearia.barbadeodin.services;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.barbearia.barbadeodin.dto.GroomingServiceDetailsDto;
import com.barbearia.barbadeodin.dto.GroomingServiceDto;
import com.barbearia.barbadeodin.dto.SpecialistDetailDto;
import com.barbearia.barbadeodin.dto.SpecialistDto;
import com.barbearia.barbadeodin.models.GroomingService;
import com.barbearia.barbadeodin.models.Specialist;
import com.barbearia.barbadeodin.repositories.GroomingServiceRepository;
import com.barbearia.barbadeodin.repositories.SpecialistRepository;

@ExtendWith(MockitoExtension.class)
class SpecialistServiceTest {

	@InjectMocks
	private SpecialistService service;
	
	@Mock
	private SpecialistRepository repository;
	@Mock
	private GroomingServiceRepository serviceRepository;
	
	private SpecialistDto specialistDto = new SpecialistDto("João lucas", "imagemUrl on", List.of(1L));
	private GroomingService serviceModel = createServiceDetailModel(1L);
	private List<GroomingService> servicosListModel = List.of(serviceModel);
	private GroomingServiceDetailsDto serviceDetailModel = new GroomingServiceDetailsDto(serviceModel);
	private List<GroomingServiceDetailsDto> expectedDetailedServices = List.of(serviceDetailModel);
	private Specialist specialistModel = createSpecialistModel(1L);
	private SpecialistDetailDto expectedDetailedSpecialist = new SpecialistDetailDto(specialistModel, expectedDetailedServices);
	
	
	@Test
	void shouldRegisterSucessfully() {
		prepareRegisterSucessFullyScenario();
		var registeredSpecialist = service.register(specialistDto);
		verifyResultRegisterScenario(registeredSpecialist);
	}
	
	
	
	

	private void verifyResultRegisterScenario(SpecialistDetailDto registeredSpecialist) {
		verifySpecialistDetailDto(registeredSpecialist);
		verifyRegisterMockBehavior();
		
	}

	private void verifyRegisterMockBehavior() {
		verify(repository, times(1)).save(any(Specialist.class));	
	}

	private void verifySpecialistDetailDto(SpecialistDetailDto DetailedSpecialist) {
		assertThat(DetailedSpecialist).isEqualTo(expectedDetailedSpecialist);	
	}

	private void prepareRegisterSucessFullyScenario() {
		when(serviceRepository.findAllById(anyList())).thenReturn(servicosListModel);
		when(repository.save(any(Specialist.class))).thenReturn(specialistModel);
	}
	
	private Specialist createSpecialistModel(Long id) {
		Specialist specialist = new Specialist(specialistDto, servicosListModel);
		specialist.setId(id);
		return specialist;
	}
	private GroomingService createServiceDetailModel(Long id) {
		GroomingService serviceModel = new GroomingService();  
		serviceModel.setId(id);
		serviceModel.setName("Corte");
		serviceModel.setDescription("Description");
		serviceModel.setPrice(20.0);
		serviceModel.setDuration(30);
		serviceModel.setCreatedAt(LocalDateTime.now().withNano(0));
		return serviceModel;
	}
	

	private GroomingServiceDto createServiceDto() {
		return new GroomingServiceDto(
				"Corte", 
				"Corte de Cabelo Profissional a pedido do cliente, finalizando no lavatório com Shampoo especializado e penteado ao final!",
				25, 30);
	};

	

}
