package com.barbearia.barbadeodin.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.barbearia.barbadeodin.dto.CustomerRequestDto;
import com.barbearia.barbadeodin.dto.CustomerResponseDto;
import com.barbearia.barbadeodin.exceptions.EmailAlreadyExistsException;
import com.barbearia.barbadeodin.models.Customer;
import com.barbearia.barbadeodin.repositories.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	@InjectMocks
	private CustomerService service;
	
	@Mock
	private CustomerRepository repository;
	
	private final CustomerRequestDto requestBody = createRequestDto();
	private final Customer customerModel = createCustomerModel(requestBody);
    
    
	
	@Test
	void shouldRegisterSucessfully() {
		stubRepositorySave();
		
		var result = service.register(requestBody);
		
		verifyRegisterResult(result);	
		verifyRegisterMockBehaviour(1);
	}
	
	@Test
	void shouldFailRegisterWithExistingEmail() {
		stubRepositoryExistsByEmail(true);
		
		var exception = assertThrows(EmailAlreadyExistsException.class, () -> service.register(requestBody));
		
		verifyExceptionResult(exception);
		verifyRegisterMockBehaviour(0);
	}
	
	@Test
	void shouldGetByIdSucessfylly() {
		stubRepositoryGetById();
		
		var result = service.getById(1L);
		
		verifyRegisterResult(result);
		verifyGetByIdMockBehaviour();
	}

	private void verifyGetByIdMockBehaviour() {
		verify(repository, times(1)).findById(any(Long.class));
	}

	private void stubRepositoryGetById() {
		when(repository.findById(any(Long.class))).thenReturn(Optional.of(customerModel));
	}

	private void verifyExceptionResult(EmailAlreadyExistsException exception) {
		assertEquals("Email já cadastrado", exception.getMessage());
	}

	private void stubRepositoryExistsByEmail(boolean exists) {
		when(repository.existsByEmail(requestBody.email())).thenReturn(exists);
	}

	private void stubRepositorySave() {
		stubRepositoryExistsByEmail(false);
		when(repository.save(any(Customer.class))).thenReturn(customerModel);
	}

	private Object createExpectedCustomerDto(Customer customerModel) {
		return new CustomerResponseDto(customerModel);
	}

	private Customer createCustomerModel(CustomerRequestDto requestBody) {
		var customer = new Customer(requestBody);
		customer.setId(1L);
		return customer;
	}

	private void verifyRegisterResult(CustomerResponseDto result) {
		var expectedCustomerDto = createExpectedCustomerDto(customerModel);
		assertNotNull(result);
		assertEquals(expectedCustomerDto, result);
	}
	
	

	private void verifyRegisterMockBehaviour(int countTimesSave) {
		verify(repository, times(1)).existsByEmail(requestBody.email());
		verify(repository, times(countTimesSave)).save(any(Customer.class));
	}

	private CustomerRequestDto createRequestDto() {
		return new CustomerRequestDto("Alisson Alves", "88999632326", "alisson@gmail.com");
	}
}
