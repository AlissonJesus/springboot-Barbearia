package com.barbearia.barbadeodin.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import com.barbearia.barbadeodin.dto.CustomerRequestDto;
import com.barbearia.barbadeodin.dto.CustomerResponseDto;
import com.barbearia.barbadeodin.models.Customer;
import com.barbearia.barbadeodin.services.CustomerService;
import com.barbearia.barbadeodin.utils.RequestSimulator;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class CustomerControllerTest {
	
	@MockBean
	private CustomerService service;
	
	@Autowired
	private JacksonTester<CustomerResponseDto> CustomerResponseJson;
	
	@Autowired
	private JacksonTester<CustomerRequestDto> CustomerRequestJson;
	
	@Autowired
	private JacksonTester<CustomerRequestDto> responseErroJson;
	
	@Autowired
	private RequestSimulator simulator;

	@Test
	void shouldRegisterSucessfully() throws Exception {
		var expectedResponseBody = prepareRegisterScenarioSimulate();
		var response = executeRegisterScenarioSimulate();
		verifyResultScenario(response, expectedResponseBody);
	}
	
	@Test
	void shouldFailRegisterWithInvalidBody() throws Exception {
		var response = executeRegisterFailScenarioSimulate("{}");
		verifyFailRegisterResultScenario(response, "Dados fornecidos inválidos");
	}
	

	private MockHttpServletResponse executeRegisterFailScenarioSimulate(String content) throws Exception {
		return simulator.simulatePostRequest("/customers", content);
	}

	private void verifyFailRegisterResultScenario(MockHttpServletResponse response, String expectedResponseBody) throws UnsupportedEncodingException {
		verifyStatus(response, HttpStatus.BAD_REQUEST);	
		verifyBody(response, expectedResponseBody);
		
		verifyRegisterMockBehavior(0);	
	}

	private void verifyResultScenario(MockHttpServletResponse response, String expectedResponseBody) throws Exception {
		verifyStatus(response, HttpStatus.CREATED);
		verifyBody(response, expectedResponseBody);
		
		verifyRegisterMockBehavior(1);	
	}

	private void verifyRegisterMockBehavior(int countTimes) {
		verify(service, times(countTimes)).register(any(CustomerRequestDto.class));
	}

	private void verifyBody(MockHttpServletResponse response, String expectedResponseBody) throws UnsupportedEncodingException {
		var body = response.getContentAsString(StandardCharsets.UTF_8);
		assertEquals(expectedResponseBody, body);
		
	}

	private void verifyStatus(MockHttpServletResponse response, HttpStatus espectedStatus) {
		assertEquals(espectedStatus.value(), response.getStatus());
	}

	private MockHttpServletResponse  executeRegisterScenarioSimulate() throws Exception {
		var requestDto = createRequestDto();
		var jsonContent = createJson(requestDto, CustomerRequestJson);
		return simulator.simulatePostRequest("/customers", jsonContent);
	}

	private CustomerRequestDto createRequestDto() {
		return new CustomerRequestDto("Alisson Alves", "88999632326", "alisson@gmail.com");
	}

	private String prepareRegisterScenarioSimulate() throws Exception {
		CustomerResponseDto expectedCustomer = createExpectedCustomer();
		when(service.register(any(CustomerRequestDto.class))).thenReturn(expectedCustomer);
		return createJson(expectedCustomer, CustomerResponseJson);
	}

	private <T> String createJson(T expectedCustomer, JacksonTester<T> customerJson) throws IOException {
		return customerJson.write(expectedCustomer).getJson();
	}

	private CustomerResponseDto createExpectedCustomer() {
		return new CustomerResponseDto(1L, "Alisson Alves", "88999632326", "alisson@gmail.com");
	}

}
