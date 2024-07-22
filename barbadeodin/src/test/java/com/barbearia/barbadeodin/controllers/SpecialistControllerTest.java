package com.barbearia.barbadeodin.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

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
	private JacksonTester<SpecialistDetailDto> spcialistDetailJson;
	
	private SpecialistDto specialistDto = new SpecialistDto("Andson Alves", "UrlImagem on");
	private SpecialistDetailDto specialistDetailDto = new SpecialistDetailDto(1L, specialistDto.name(), specialistDto.imagemUrl());


	@Test
	void shouldRegisterSuccessfully() throws Exception {
		prepareScenarioSimulateRegister();
		var response = executeScenarioSimulateRegister();
		verifyResultScenarioSimulateRegister(response);
	}

	private void verifyResultScenarioSimulateRegister(MockHttpServletResponse response) throws Exception {
		verifyStatus(response, HttpStatus.CREATED);
		verifyBody(response);
		verifyMockBehaviorRegister();
	}

	private void verifyMockBehaviorRegister() {
		verify(service, times(1)).register(any(SpecialistDto.class));
	}

	private void verifyBody(MockHttpServletResponse response) throws IOException {
		var body = response.getContentAsString(StandardCharsets.UTF_8);
		var expectedJson = createJson(specialistDetailDto, spcialistDetailJson);
		assertThat(body).isEqualTo(expectedJson);
	}

	private void verifyStatus(MockHttpServletResponse response, HttpStatus status) {
		assertThat(response.getStatus()).isEqualTo(status.value());
	}

	private <T> String createJson(T specialist, JacksonTester<T> specialistJson) throws IOException {
		return specialistJson.write(specialist).getJson();
	}

	private MockHttpServletResponse executeScenarioSimulateRegister() throws Exception {
		String jsonContent = createJson(specialistDto, specialistJson);
		return simulator.simulatePostRequest("/specialists", jsonContent);
	}

	private void prepareScenarioSimulateRegister() {
		when(service.register(any(SpecialistDto.class))).thenReturn(specialistDetailDto);	
	}

}
