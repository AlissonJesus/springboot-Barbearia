package com.barbearia.barbadeodin.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
import com.barbearia.barbadeodin.dto.SpecialistDetailDto;
import com.barbearia.barbadeodin.dto.SpecialistDto;

import com.barbearia.barbadeodin.services.SpecialistService;
import com.barbearia.barbadeodin.utils.RequestSimulator;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class SpecialistControllerTest {
	
	@Autowired
	private RequestSimulator simulator;
	
	@MockBean
	private SpecialistService service;
	
	@Autowired
	private JacksonTester<SpecialistDto> specialistJson;
	
	@Autowired
	private JacksonTester<SpecialistDetailDto> specialistDetailJson;
	
	@Autowired
	private JacksonTester<List<SpecialistDetailDto>> spcialistDetailListJson;
	
	private SpecialistDto specialistDto = new SpecialistDto("João lucas", "imagemUrl on", List.of(1L));
	private GroomingServiceDto payload = new GroomingServiceDto(
			"Corte", 
			"Corte de Cabelo Profissional a pedido do cliente, finalizando no lavatório com Shampoo especializado e penteado ao final!",
			25, 30);
	private GroomingServiceDetailsDto serviceDetail = createServiceDetailDto(1L, payload.name(), payload.price());
	private List<GroomingServiceDetailsDto> services = List.of(serviceDetail);
	private SpecialistDetailDto specialistDetailDto = new SpecialistDetailDto(1L, specialistDto.name(), specialistDto.imagemUrl(), services);
	private List<SpecialistDetailDto> specialistDetailList = List.of(specialistDetailDto);


	@Test
	void shouldRegisterSuccessfully() throws Exception {
		prepareRegisterSimulationScenario();
		var response = executeRegisterScenarioSimulate();
		verifyResultScenarioSimulateRegister(response);
	}
	
	@Test
	void shouldGetByIdSucessfully() throws Exception {
		prepareGetByIdSimulationScenario();
		var response = executeGetByIdScenarioSimulate();
		verifyResultGetByIdSimulationScenario(response);
	}
	
	@Test
	void shouldGetAllSucessFully() throws Exception {
		prepareGetAllSimulationScenario();
		var response = executeGetAllRequestSimulate();
		verifyResultGetAllRequestSimulation(response);
	}
	
	@Test
	void shouldUpdateByIdSucessFully() throws Exception {
		prepareUpdateByIdSimulate();
		MockHttpServletResponse response = executeUpdateByIdSimulate();
		verifyResultUpdateByIdSimulate(response);
	}
	
	@Test
	void shouldDeleteByIdSucessFully() throws Exception {
		prepareDeleteByIdSimulate();
		var response = executeDeleteByIdSimulate();
		verifyResultDeleteByIdSimulate(response);
	}

	private void verifyResultDeleteByIdSimulate(MockHttpServletResponse response) {
		verifyStatus(response, HttpStatus.NO_CONTENT);
		verifyDeleteByIdMockBehavior();
	}

	private void verifyDeleteByIdMockBehavior() {
		verify(service, times(1)).deleteById(any(Long.class));
	}

	private MockHttpServletResponse executeDeleteByIdSimulate() throws Exception {
		return simulator.simulateDeleteRequest("/specialists/{id}", 1L);
	}

	private void prepareDeleteByIdSimulate() {
		doNothing().when(service).deleteById(any(Long.class));
	}

	private void verifyResultUpdateByIdSimulate(MockHttpServletResponse response) throws IOException {
		verifyStatus(response, HttpStatus.OK);
		verifyBody(response, specialistDetailDto, specialistDetailJson);
		
		verifyUpdateByIdMockBehavior();
	}

	private void verifyUpdateByIdMockBehavior() {
		verify(service, times(1)).updateById(any(Long.class));
	}

	private MockHttpServletResponse executeUpdateByIdSimulate() throws IOException, Exception {
		return simulator.simulatePutRequest("/specialists/{id}", 1L, createJson(specialistDto, specialistJson));
	}

	private void prepareUpdateByIdSimulate() {
		when(service.updateById(any(Long.class))).thenReturn(specialistDetailDto);	
	}

	private void verifyResultGetAllRequestSimulation(MockHttpServletResponse response) throws IOException {
		verifyStatus(response, HttpStatus.OK);
		verifyBody(response, specialistDetailList, spcialistDetailListJson);
		
		verifyGetAllMockBehavior();
		
	}

	private void verifyGetAllMockBehavior() {
		verify(service, times(1)).getAll();
		
	}

	private MockHttpServletResponse executeGetAllRequestSimulate() throws Exception {
		return simulator.simulateGetRequest("/specialists");
		
	}

	private void prepareGetAllSimulationScenario() {		
		when(service.getAll()).thenReturn(specialistDetailList);
		
	}

	private void verifyResultGetByIdSimulationScenario(MockHttpServletResponse response) throws IOException {
		verifyStatus(response, HttpStatus.OK);
		verifyBody(response, specialistDetailDto, specialistDetailJson);		
	}

	private MockHttpServletResponse executeGetByIdScenarioSimulate() throws Exception {
		return simulator.simulateGetRequest("/specialists/{id}", 1L);
	}

	private void prepareGetByIdSimulationScenario() {
		when(service.getById(any(Long.class))).thenReturn(specialistDetailDto);
	}

	private void verifyResultScenarioSimulateRegister(MockHttpServletResponse response) throws Exception {
		verifyStatus(response, HttpStatus.CREATED);
		verifyBody(response, specialistDetailDto, specialistDetailJson);
		verifyMockBehaviorRegister();
	}

	private void verifyMockBehaviorRegister() {
		verify(service, times(1)).register(any(SpecialistDto.class));
	}

	private <T> void verifyBody(MockHttpServletResponse response, T detailedDto, JacksonTester<T> jsonCreator) throws IOException {
		var body = response.getContentAsString(StandardCharsets.UTF_8);
		var expectedJson = createJson(detailedDto, jsonCreator);
		assertThat(body).isEqualTo(expectedJson);
	}

	private void verifyStatus(MockHttpServletResponse response, HttpStatus status) {
		assertThat(response.getStatus()).isEqualTo(status.value());
	}

	private <T> String createJson(T specialist, JacksonTester<T> specialistJson) throws IOException {
		return specialistJson.write(specialist).getJson();
	}

	private MockHttpServletResponse executeRegisterScenarioSimulate() throws Exception {
		String jsonContent = createJson(specialistDto, specialistJson);
		return simulator.simulatePostRequest("/specialists", jsonContent);
	}

	private void prepareRegisterSimulationScenario() {
		when(service.register(any(SpecialistDto.class))).thenReturn(specialistDetailDto);	
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
