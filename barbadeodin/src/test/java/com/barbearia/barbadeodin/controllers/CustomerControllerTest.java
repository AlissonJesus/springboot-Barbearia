package com.barbearia.barbadeodin.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import com.barbearia.barbadeodin.dto.CustomerRequestDto;
import com.barbearia.barbadeodin.dto.CustomerResponseDto;
import com.barbearia.barbadeodin.models.Customer;
import com.barbearia.barbadeodin.services.CustomerService;
import com.barbearia.barbadeodin.utils.RequestSimulator;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class CustomerControllerTest {
	
	@MockBean
	private CustomerService service;
	
	@Autowired
	private JacksonTester<CustomerResponseDto> CustomerResponseJson;
	
	@Autowired
	private JacksonTester<List<CustomerResponseDto>> CustomerListResponseJson;
	
	@Autowired
	private JacksonTester<CustomerRequestDto> CustomerRequestJson;
	
	@Autowired
	private JacksonTester<CustomerRequestDto> responseErroJson;
	
	@Autowired
	private RequestSimulator simulator;
	
	private MockHttpServletResponse response;

	@Test
	void shouldRegisterSucessfully() throws Exception {
		var expectedResponseBody = prepareRegisterScenarioSimulate();
		response = executeRegisterScenarioSimulate();
		verifyRegisterResultScenario(expectedResponseBody, HttpStatus.CREATED);
	}
	
	@Test
	void shouldFailRegisterWithInvalidBody() throws Exception {
		response = executeRegisterFailScenarioSimulate("{}");
		verifyFailRegisterResultScenario("Dados fornecidos inválidos", HttpStatus.BAD_REQUEST);
	}
	
	@Test
	void shouldGetByIdSucessfylly() throws Exception  {
		var expectedResponseBody = prepareGetByIdScenarioSimulate();
		response = executeGetByIdScenarioSimulate();
		verifyGetByIdResultScenario(expectedResponseBody, HttpStatus.OK);
	}
	
	@Test
	void shouldFailGetByIdWithInvalidId() throws Exception {
		var expectedResponseBody = prepareFailGetByIdScenarioSimulate();
		response = executeGetByIdScenarioSimulate();
		verifyGetByIdResultScenario(expectedResponseBody, HttpStatus.NOT_FOUND);
		
	}
	
	@Test
	void shouldGetAllSucessfully() throws Exception {
		var expectedResponseBody = prepareFailGetAllScenarioSimulate();
		response = executeGetAllScenarioSimulate();
		verifyGetAllResultScenario(expectedResponseBody, HttpStatus.OK);
	}
	
	@Test
	void shouldUpdateByIdSucessfully() throws Exception {
		var expectedResponseBody = prepareUpdateByIdScenarioSimulate();
		response = executeUpdateByIdcenarioSimulate();
		verifyUpdateByIdResultScenario(expectedResponseBody, HttpStatus.OK);
	}
	
	@Test
	void shouldFailUpdateByIdWithInvalidId() throws Exception {
		var expectedResponseBody = prepareFailUpdateByIdScenarioSimulate();
		response = executeUpdateByIdcenarioSimulate();
		verifyUpdateByIdResultScenario(expectedResponseBody, HttpStatus.NOT_FOUND);
	}	
	
	@Test
	void shouldDeleteByIdSucessfully() throws Exception {
		prepareDeleteByIdScenarioSimulate();
		var response = executeDeleteByIdScenarioSimulate();
		verifyDeleteByIdResultScenario(response, HttpStatus.NO_CONTENT);
	}

	private void verifyDeleteByIdResultScenario(MockHttpServletResponse response, HttpStatus noContent) {
		verifyStatus(response, noContent);
		verifyDeleteByIdMockBehaviour(1);
	}

	private void verifyDeleteByIdMockBehaviour(int countTimes) {
		verify(service, times(countTimes)).deleteById(any(Long.class));
	}

	private void prepareDeleteByIdScenarioSimulate() {
		doNothing().when(service).deleteById(any(Long.class));
		
	}

	private MockHttpServletResponse executeDeleteByIdScenarioSimulate() throws Exception {
		return simulator.simulateDeleteRequest("/customers/{id}", 1L);
	}

	private String prepareFailUpdateByIdScenarioSimulate() {
		var message = "Cliente não encontrado";
		when(service.updateById(any(Long.class), any(CustomerRequestDto.class)))
		.thenThrow(new EntityNotFoundException(message));
		return message;
	}

	private void verifyUpdateByIdResultScenario(String expectedResponseBody, HttpStatus status) throws UnsupportedEncodingException {
		verifyResponse(expectedResponseBody, status);
		verifyUpdateByIdMockBehaviour(1);
	}

	private void verifyUpdateByIdMockBehaviour(int countTimes) {
		verify(service, times(countTimes)).updateById(any(Long.class), any(CustomerRequestDto.class));
	}

	private MockHttpServletResponse executeUpdateByIdcenarioSimulate() throws Exception {
		var requestDto = createRequestDto();
		var jsonContent = createJson(requestDto, CustomerRequestJson);
		return simulator.simulatePutRequest("/customers/{id}", 1L, jsonContent);
	}

	private String prepareUpdateByIdScenarioSimulate() throws IOException {
		var responseBody = createExpectedCustomer();
		var expectedCustomer = createJson(responseBody, CustomerResponseJson);
		when(service.updateById(any(Long.class), any(CustomerRequestDto.class))).thenReturn(responseBody);
		return expectedCustomer;
	}

	private void verifyGetAllResultScenario(String expectedResponseBody, HttpStatus status) throws UnsupportedEncodingException {
		verifyResponse(expectedResponseBody, status);
		verifyGetAllMockBehaviour(1);
	}

	private void verifyGetAllMockBehaviour(int countTimes) {
		verify(service, times(countTimes)).getAll();
	}

	private MockHttpServletResponse executeGetAllScenarioSimulate() throws Exception {
		return simulator.simulateGetRequest("/customers");
	}

	private String prepareFailGetAllScenarioSimulate() throws IOException {
		var customer = createExpectedCustomer();
		List<CustomerResponseDto> expectedCustomers = List.of(customer); 
		when(service.getAll()).thenReturn(expectedCustomers);
		return createJson(expectedCustomers, CustomerListResponseJson);
	}

	private String prepareFailGetByIdScenarioSimulate() {
		var message = "Cliente não encontrado";
		when(service.getById(any(Long.class))).thenThrow(new EntityNotFoundException(message));
		return message;
	}

	private void verifyGetByIdResultScenario(String expectedResponseBody, HttpStatus status) throws UnsupportedEncodingException {
		verifyResponse(expectedResponseBody, status);
		verifyGetByIdMockBehavior(1);	
	}

	private void verifyGetByIdMockBehavior(int countTimes) {
		verify(service, times(countTimes)).getById(any(Long.class));
	}

	private MockHttpServletResponse executeGetByIdScenarioSimulate() throws Exception {
		return simulator.simulateGetRequest("/customers/{id}", 1L);
	}
	

	private String prepareGetByIdScenarioSimulate() throws IOException {
		when(service.getById(any(Long.class))).thenReturn(createExpectedCustomer());
		return createJson(createExpectedCustomer(), CustomerResponseJson);
	}

	private MockHttpServletResponse executeRegisterFailScenarioSimulate(String content) throws Exception {
		return simulator.simulatePostRequest("/customers", content);
	}

	private void verifyFailRegisterResultScenario(String expectedResponseBody, HttpStatus status) throws UnsupportedEncodingException {
		verifyResponse(expectedResponseBody, status);
		verifyRegisterMockBehavior(0);	
	}

	private void verifyRegisterResultScenario(String expectedResponseBody, HttpStatus status) throws Exception {
		verifyResponse(expectedResponseBody, status);
		verifyRegisterMockBehavior(1);	
	}

	private void verifyResponse(String expectedResponseBody, HttpStatus status) throws UnsupportedEncodingException {
		verifyStatus(response, status);
		verifyBody(response, expectedResponseBody);
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
