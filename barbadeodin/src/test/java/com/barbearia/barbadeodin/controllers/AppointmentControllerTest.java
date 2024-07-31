package com.barbearia.barbadeodin.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import com.barbearia.barbadeodin.dto.AppointmentRequestDto;
import com.barbearia.barbadeodin.dto.AppointmentResponseDto;
import com.barbearia.barbadeodin.services.AppointmentService;
import com.barbearia.barbadeodin.utils.RequestSimulator;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AppointmentControllerTest {

	@MockBean
	private AppointmentService service;
	
	
	@Autowired
	private RequestSimulator simulator;
	
	@Autowired
	private JacksonTester<AppointmentRequestDto> requestBodyJson;
	
	@Autowired
	private JacksonTester<AppointmentResponseDto> responseBodyJson;
	
	private LocalDateTime date = LocalDateTime.now().plusHours(1);
	private MockHttpServletResponse response;
	
	@Test
	void shouldRegisterSuccessfully() throws Exception {
		var responseBodyDto = createResponseBodyDto();
		var requestBody = prepareRegisterSimulate(responseBodyDto);
		
		var expectedResponseBody = executeRegisterSimulate(responseBodyDto, requestBody);
		
		verifyExpectedResult(response, expectedResponseBody);
		verifyMockBehaviour();
	}
	
	private String executeRegisterSimulate(AppointmentResponseDto responseBodyDto, String requestBody) throws Exception {
		response = simulator.simulatePostRequest("/appointments", requestBody);
		return createJson(responseBodyDto, responseBodyJson);
	}
	
	private String prepareRegisterSimulate(AppointmentResponseDto responseBodyDto) throws IOException {
		var requestBodyDto = createRequestBodyDto();
		var requestBody = createJson(requestBodyDto, requestBodyJson);
		
		when(service.register(any(AppointmentRequestDto.class))).thenReturn(responseBodyDto);
		return requestBody;
	}
	

	private AppointmentResponseDto createResponseBodyDto() {
		return new AppointmentResponseDto(1L, 1L, 1L, date, "Notas novas");
	}

	private AppointmentRequestDto createRequestBodyDto() {
		return new AppointmentRequestDto(1L, 1L, date, "Notas novas");
	}

	private <T> String createJson(T bodyDto, JacksonTester<T> bodyJson) throws IOException {
		return bodyJson.write(bodyDto).getJson();
	}

	private void verifyMockBehaviour() {
		verify(service, times(1)).register(any(AppointmentRequestDto.class));
	}

	private void verifyExpectedResult(MockHttpServletResponse response, String expectedResponseBody) throws UnsupportedEncodingException {
		verifyStatus(response, HttpStatus.CREATED);
		
		var body = response.getContentAsString(StandardCharsets.UTF_8);
		verifyResponseBody(expectedResponseBody, body);
	}

	private void verifyStatus(MockHttpServletResponse response, HttpStatus expectedStatus) {
		assertEquals(expectedStatus.value(), response.getStatus());		
	}

	private void verifyResponseBody(String body, String expectedBody) {
		assertEquals(expectedBody, body);
	}

}
