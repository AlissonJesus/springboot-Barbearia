package com.barbearia.barbadeodin.integrations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.barbearia.barbadeodin.dto.GroomingServiceDetailsDto;
import com.barbearia.barbadeodin.dto.GroomingServiceDto;
import com.barbearia.barbadeodin.models.GroomingService;
import com.barbearia.barbadeodin.repositories.GroomingServiceRepository;
import com.barbearia.barbadeodin.utils.RequestSimulator;

import jakarta.transaction.Transactional;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class GroomingServiceIntegrationTest {

	@Autowired
    private GroomingServiceRepository repository;
    
    @Autowired
    private RequestSimulator simulator;
    
    @Autowired
    private JacksonTester<GroomingServiceDto> payloadJson;
    
    @Autowired
    private JacksonTester<GroomingServiceDetailsDto> serviceDetailsJson;
    
    @Autowired
    private JacksonTester<List<GroomingServiceDetailsDto>> serviceDetailsListJson;
    
    
    private GroomingServiceDto serviceDto = new GroomingServiceDto(
            "Corte", 
            "Corte de Cabelo Profissional a pedido do cliente, finalizando no lavatório com Shampoo especializado e penteado ao final!",
            25, 30);;
     
    private GroomingService serviceModel;
    private GroomingServiceDetailsDto serviceDetailsDto = createServiceDetailDto(2L, serviceDto.name(), serviceDto.price());
    private List<GroomingServiceDetailsDto> serviceDetialDtoList;

    @BeforeEach
    void setUp() {
        serviceModel = createGroomingServiceModel(null, serviceDto.name(), serviceDto.price());
        repository.save(serviceModel);
    }
	
	@Test
	void shouldRegisterSuccessfully() throws Exception {
		var jsonContent = createJson(serviceDto, payloadJson);
		var response = simulator.simulatePostRequest("/services", jsonContent);
		//verifyResponseOneService(response, serviceDetailsDto);
	}

	@Test
	void shouldGetByIdSuccessfully() throws Exception {
		var model = registerService(serviceModel);
		var response = simulator.simulateGetRequest("/services/{id}", model.getId());
		verifyResponseOneService(response, new GroomingServiceDetailsDto(model));
	}
	
	@Test
	void shouldGetAllSuccessfully() throws Exception {
		generateGroomingServiceDetailList();
		var response = simulator.simulateGetRequest("/services");
		verifyResponseAllService(response);
		
	}
	
	private void verifyResponseAllService(MockHttpServletResponse response) throws IOException {
		var expectedBody = createJson(serviceDetialDtoList, serviceDetailsListJson);
		var responseBody = response.getContentAsString(StandardCharsets.UTF_8);
		
		assertNotNull(responseBody);
		assertThat(responseBody).isEqualTo(expectedBody);
	}

	private GroomingService createGroomingServiceModel(Long id, String name, Double price) {
		var serviceModel = new GroomingService(serviceDto);
		serviceModel.setId(id);
		return serviceModel;
	}
	
	
	private GroomingService registerService(GroomingService serviceModel) {
		return repository.save(serviceModel);
	}
	
	private void verifyResponseOneService(MockHttpServletResponse response, GroomingServiceDetailsDto service) throws IOException {
		var expectedBody = createJson(service, serviceDetailsJson);
		var responseBody = response.getContentAsString(StandardCharsets.UTF_8);
		
		assertNotNull(responseBody);
		assertThat(responseBody).isEqualTo(expectedBody);
	}

	private <T> String createJson(T payload, JacksonTester<T> payloadJson) throws IOException {
		return payloadJson.write(payload).getJson();
	}
	

	
	private void generateGroomingServiceDetailList() {
		var serviceModel1 = registerService(serviceModel);
		var serviceModel2 = registerService(createGroomingServiceModel(null, "barba", 20.0));
		
		serviceDetialDtoList = List.of(
				new GroomingServiceDetailsDto(serviceModel1),
				new GroomingServiceDetailsDto(serviceModel2));
	}
	
	private GroomingServiceDetailsDto createServiceDetailDto(Long id, String name, double price) {
		return new GroomingServiceDetailsDto(
				id,
				name, 
				serviceDto.description(),
				price, 
				serviceDto.duration());
	};
	
	

}
