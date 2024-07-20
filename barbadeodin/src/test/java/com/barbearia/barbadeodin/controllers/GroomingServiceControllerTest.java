package com.barbearia.barbadeodin.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import org.springframework.mock.web.MockHttpServletResponse;

import com.barbearia.barbadeodin.dto.GroomingServiceDetailsDto;
import com.barbearia.barbadeodin.dto.GroomingServiceDto;
import com.barbearia.barbadeodin.services.GroomingServiceService;
import com.barbearia.barbadeodin.utils.RequestSimulator;
import com.barbearia.barbadeodin.utils.ServiceResponseVerifier;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class GroomingServiceControllerTest {
	
	@MockBean
	private GroomingServiceService service;
	
	@Autowired
	private RequestSimulator requestSimulator;
	
	@Autowired
	private JacksonTester<GroomingServiceDto> payloadJson;
	
	private MockHttpServletResponse response;
	
	private GroomingServiceDto payload = new GroomingServiceDto(
			"Corte", 
			"Corte de Cabelo Profissional a pedido do cliente, finalizando no lavatório com Shampoo especializado e penteado ao final!",
			25, 30);
	
	private GroomingServiceDetailsDto serviceDetail = createServiceDetailDto(1L, "Corte", 20.0);
	
	private String methodArgumentNotValidMessage = "Dados inválidos fornecidos";
	private String invalidIdMessage = "Serviço não encontrado";
	
	@Test
	@DisplayName("Should register successfully and return status 200")
	void shouldRegisterServiceSuccessfully() throws Exception {
		var requestBody = prepareSimulationScenarioForRegistration();
		response = executeRegistrationSimulation(requestBody);
		verifyResultRegistrationSimulation();
	}
	
	
	@Test
	@DisplayName("Should fail to register with invalid request body and return status 400")
	void shouldFailRegisterInvalidRequestBody() throws Exception {
		var response = executeRegistrationSimulation("{}");
		ServiceResponseVerifier.verifyErrorResponse(response, HttpStatus.BAD_REQUEST, methodArgumentNotValidMessage);
	}
	
	@Test
	void shouldGetServiceSucessfully() throws Exception {
		prepareSimulationScenarioForGetById();
		var response = executeGetByIdScenarioSimulation();
		ServiceResponseVerifier.verifyExpectedResponseRegister(response, HttpStatus.OK, serviceDetail);
		verifyMockBehaviorGetById();
	}
	
	@Test
	void shouldFailGetServiceInvalidId() throws Exception {
		prepareFailSimulationScenarioGetById();
		var response = executeGetByIdScenarioSimulation();
		ServiceResponseVerifier.verifyErrorResponse(response, HttpStatus.NOT_FOUND, invalidIdMessage);
		verifyMockBehaviorGetById();
	}
	
	@Test
	void shouldGetAllServices() throws Exception {
		prepareSimulationScenarioGetAll();
		var response = executeGetAllScenarioSimulation();
		ServiceResponseVerifier.verifyExpectedResponseGetAll(response, HttpStatus.OK, serviceDetail);
		verifyMockBehaviorGetAll();
	}
	
	@Test
	void shouldUpdateByIdSucessfully() throws Exception {
		var requestBody = prepareSimulationScenarioUpdateById();
		var response = executeUpateByIdScenarioSimulation(requestBody);
		ServiceResponseVerifier.verifyExpectedResponseRegister(response, HttpStatus.OK, serviceDetail);
		verifyMockBehaviorUpdateById();
	}
	
	@Test
	void shouldDeleteByIdSucessfully() throws Exception {
	
		prepareSimulationScenarioDeleteById();
		var response = executeDeleteByIdScenarioSimulation();
		ServiceResponseVerifier.verifyExpectedResponseDeleteById(response, HttpStatus.NO_CONTENT);
		verifyMockBehaviorDeleteById();
	}
	
	private void prepareSimulationScenarioDeleteById() {
		doNothing().when(service).deleteById(1L);
	}
	
	private String prepareSimulationScenarioUpdateById() throws IOException {
		when(service.updateById(1L, payload)).thenReturn(serviceDetail);
		return createExpectedJson(payload, payloadJson);
	}
	
	private void prepareSimulationScenarioGetAll() {
		when(service.getAll()).thenReturn(createServiceDetailsList());	
	}
	
	private void prepareFailSimulationScenarioGetById() {
		when(service.getById(any(Long.class))).thenThrow(new EntityNotFoundException());	
	}
	

	private void prepareSimulationScenarioForGetById() {
		when(service.getById(any(Long.class))).thenReturn(serviceDetail);
	}
	
	private String prepareSimulationScenarioForRegistration() throws IOException {
		when(service.register(payload)).thenReturn(serviceDetail);
		return createExpectedJson(payload, payloadJson);
	}
	
	private MockHttpServletResponse executeRegistrationSimulation(String requestBody) throws Exception {
		return requestSimulator.simulatePostRequest("/services", requestBody);
	}	
	
	private MockHttpServletResponse executeGetAllScenarioSimulation() throws Exception {
		return requestSimulator.simulateGetRequest("/services");
	}
	
	private MockHttpServletResponse executeGetByIdScenarioSimulation() throws Exception {
		return requestSimulator.simulateGetRequest("/services/{id}", 1L);
	}	
	
	private MockHttpServletResponse executeUpateByIdScenarioSimulation(String requestBody) throws Exception {
		return requestSimulator.simulatePutRequest("/services/{id}", 1L, requestBody);
	}
	
	private MockHttpServletResponse executeDeleteByIdScenarioSimulation() throws Exception {
		return requestSimulator.simulateDeleteRequest("/services/{id}", 1L);
	}
	
	private void verifyResultRegistrationSimulation() throws Exception {
		ServiceResponseVerifier.verifyExpectedResponseRegister(response, HttpStatus.CREATED, serviceDetail);
		verifyMockBehaviorRegister();
	}
	

	private void verifyMockBehaviorDeleteById() {
		verify(service, times(1)).deleteById(any(Long.class));	
	}
	
	private void verifyMockBehaviorGetAll() {
		verify(service, times(1)).getAll();	
	}

	private void verifyMockBehaviorRegister() {
		verify(service, times(1)).register(payload);	
	}
	
	private void verifyMockBehaviorGetById() {
		verify(service, times(1)).getById(any(Long.class));	
	}
	
	private void verifyMockBehaviorUpdateById() {
		verify(service, times(1)).updateById(1L, payload);	
	}
	
	private <T> String createExpectedJson(T payload, JacksonTester<T> payloadJson) throws IOException {
		return payloadJson.write(payload).getJson();
	}
	
	private List<GroomingServiceDetailsDto> createServiceDetailsList() {
		return List.of(serviceDetail);
	}
	
	private GroomingServiceDetailsDto createServiceDetailDto(Long id, String name, double price) {
		return new GroomingServiceDetailsDto(
				id,
				name, 
				payload.description(),
				price, 
				payload.duration());
	};
}
