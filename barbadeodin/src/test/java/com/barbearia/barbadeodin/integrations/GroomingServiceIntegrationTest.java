package com.barbearia.barbadeodin.integrations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

import com.barbearia.barbadeodin.dto.GroomingServiceDetailsDto;
import com.barbearia.barbadeodin.dto.GroomingServiceDto;

import com.barbearia.barbadeodin.repositories.GroomingServiceRepository;
import com.barbearia.barbadeodin.utils.RequestSimulator;


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
	
	private GroomingServiceDto serviceDto = new GroomingServiceDto(
			"Corte", 
			"Corte de Cabelo Profissional a pedido do cliente, finalizando no lavatório "
			+ "com Shampoo especializado e penteado ao final!",
			25, 30);
	private GroomingServiceDetailsDto serviceDetailsDto = createServiceDetailDto(
			1L, 
			serviceDto.name(), 
			serviceDto.price());
	
	
	
	
	@Test
	void shouldRegisterSuccessfully() throws Exception {
		var jsonContent = createJson(serviceDto, payloadJson);
		var response = simulator.simulatePostRequest("/services", jsonContent);
		verifyResponseRegister(response);
	}
	
	
	
	private void verifyResponseRegister(MockHttpServletResponse response) throws IOException {
		var expectedBody = createJson(serviceDetailsDto, serviceDetailsJson);
		var responseBody = response.getContentAsString(StandardCharsets.UTF_8);
		assertNotNull(responseBody);
		
		assertThat(responseBody).isEqualTo(expectedBody);
	}

	private <T> String createJson(T payload, JacksonTester<T> payloadJson) throws IOException {
		return payloadJson.write(payload).getJson();
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
