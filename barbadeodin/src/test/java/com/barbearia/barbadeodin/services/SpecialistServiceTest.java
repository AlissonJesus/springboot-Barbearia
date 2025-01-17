package com.barbearia.barbadeodin.services;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
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
import com.barbearia.barbadeodin.repositories.ServiceIdOnly;
import com.barbearia.barbadeodin.repositories.SpecialistProjection;
import com.barbearia.barbadeodin.repositories.SpecialistRepository;

@ExtendWith(MockitoExtension.class)
class SpecialistServiceTest {

	@InjectMocks
	private SpecialistService service;
	
	@Mock
	private SpecialistRepository repository;
	@Mock
	private GroomingServiceRepository serviceRepository;
	
	private SpecialistProjection specialistProjection = prepareSpecialistProjection();
	
	private SpecialistDto specialistDto = new SpecialistDto("João lucas", "imagemUrl on", List.of(1L));
	private GroomingService serviceModel = createServiceDetailModel(1L);
	private List<GroomingService> servicosListModel = List.of(serviceModel);
	private List<Long> expectedServiceIds = List.of(1L);
	private Specialist specialistModel = createSpecialistModel(1L);
	private SpecialistDetailDto expectedDetailedSpecialist = new SpecialistDetailDto(specialistModel, expectedServiceIds);
	

	
	
	@Test
	void shouldRegisterSucessfully() {
		prepareRegisterSucessFullyScenario();
		var registeredSpecialist = service.register(specialistDto);
		verifyResultRegisterScenario(registeredSpecialist);
	}	
	
	@Test
	void shouldGetByIdSucessfully() {
		prepareGetByIdScenario();
		var result = service.getById(1L);
		verifyResultGetByIdScenario(result);
	}
	
	@Test
	void shouldGetAllSucessfully() {
		prepareGetAllScenario();
		var result = service.getAll();
		verifyResultGetAllScenario(result);
	}
	
	@Test
	void shouldDeletaByIdSucessfully() {
		prepareDeletaByIdScenario();
		service.deleteById(1L);
		verifyResultDeletaByIdScenario();
	}

	private void verifyResultDeletaByIdScenario() {
		verify(repository, times(1)).deleteById(1L);	
	}

	private void prepareDeletaByIdScenario() {
		doNothing().when(repository).deleteById(any(Long.class));
		
	}

	private void verifyResultGetAllScenario(List<SpecialistDetailDto> result) {
		verifySpecialistDetailDto(result.get(0));
		verifyGetAllMockBehavior();
		
	}

	private void verifyGetAllMockBehavior() {
		verify(repository, times(1)).findAllWithServiceIds();
	}

	private void prepareGetAllScenario() {
		var specialists = List.of(specialistProjection);
		when(repository.findAllWithServiceIds()).thenReturn(specialists);
		
	}

	private void verifyResultGetByIdScenario(SpecialistDetailDto result) {
		verifySpecialistDetailDto(result);
		verifyGetByIdMockBehavior();
	}

	private void verifyGetByIdMockBehavior() {
		verify(repository, times(1)).findSpecialistById(any(Long.class));	
	}

	private void prepareGetByIdScenario() {
		when(repository.findSpecialistById(any(Long.class))).thenReturn(Optional.of(specialistProjection));
	}

	private void verifyResultRegisterScenario(SpecialistDetailDto registeredSpecialist) {
		verifySpecialistDetailDto(registeredSpecialist);
		verifyRegisterMockBehavior();
		
	}

	private void verifyRegisterMockBehavior() {
		verify(serviceRepository, times(1)).findAllById(expectedServiceIds);
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
	
	private SpecialistProjection prepareSpecialistProjection() {
		return new SpecialistProjection() {
            @Override
            public Long getId() {
                return expectedDetailedSpecialist.id();
            }
            @Override
            public String getName() {
                return specialistDto.name();
            }
            @Override
            public String getImagemUrl() {
                return specialistDto.imagemUrl();
            }
            @Override
            public List<ServiceIdOnly> getServices() {
                return Stream.of(1L)
                        .map(id -> new ServiceIdOnly() {
                            @Override
                            public Long getId() {
                                return id;
                            }
                        })
                        .collect(Collectors.toList());
            }
        };
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
