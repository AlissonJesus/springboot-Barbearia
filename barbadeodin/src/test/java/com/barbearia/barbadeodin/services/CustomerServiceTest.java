package com.barbearia.barbadeodin.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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

import jakarta.persistence.EntityNotFoundException;

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
		
		verifyExceptionResult(exception, "Email já cadastrado");
		verifyRegisterMockBehaviour(0);
	}
	
	@Test
	void shouldGetByIdSucessfylly() {
		stubRepositoryGetById(Optional.of(customerModel));
		
		var result = service.getById(1L);

		verifyRegisterResult(result);
		verifyGetByIdMockBehaviour();
	}
	

	@Test
	void shouldFailGetByIdWithInvalidId() {
		stubRepositoryGetById(Optional.empty());
		
		
		var exception = assertThrows(EntityNotFoundException.class, () -> service.getById(1L));
		verifyExceptionResult(exception, "Cliente não encontrado");
		
		verifyGetByIdMockBehaviour();
	}
	
	@Test
	void shouldGetAllSSucessfully() {
		stubRepositoryGetAll();
		var result = service.getAll();
		var firstModel = result.get(0);
		
		verifyRegisterResult(firstModel);
		verifyGetAllMockBehaviour();
	}
	
	@Test
	void shouldUpdateByIdSucessfully() {
		stubRepositoryExistsByEmail(false);
		stubRepositoryGetReferenceById();
		
		var result = service.updateById(1L, requestBody);
	
		verifyRegisterResult(result);
		verifyUpdateByIdMockBehaviour();
	}
	
	private void stubRepositoryGetReferenceById() {
		Customer customerInitial = createCustomerModel("Andson Alves","alisson@Alves.com");
		when(repository.getReferenceById(any(Long.class))).thenReturn(customerInitial);
	}

	@Test
	void shouldFailUpdateIdWithInvalidId() {
		stubRepositoryFailGetById();
		
		var exception = assertThrows(EntityNotFoundException.class, () -> service.updateById(1L, requestBody));
		
		verifyExceptionResult(exception, "Cliente não encontrado");
		verifyUpdateByIdMockBehaviour();
	}
	
	@Test
	void shouldFailUpdateByIdWithExistingEmail() {
		stubRepositoryUpdateById();
		
		var exception = assertThrows(EmailAlreadyExistsException.class, () -> service.updateById(1L, requestBody));
		
		verifyExceptionResult(exception, "Email já cadastrado");
		verifyRegisterMockBehaviour(0);
	}



	private void stubRepositoryFailGetById() {
		when(repository.getReferenceById(any(Long.class))).thenThrow(new EntityNotFoundException("Cliente não encontrado"));	
	}

	private void verifyUpdateByIdMockBehaviour() {
		verify(repository, times(1)).getReferenceById(1L);
	}

	private void stubRepositoryUpdateById() {
		Customer customerInitial = createCustomerModel("Alisson Alves","alisson@Alves.com");
		stubRepositoryExistsByEmail(true);
		when(repository.getReferenceById(any(Long.class))).thenReturn(customerInitial);
	}

	private Customer createCustomerModel(String name, String email) {
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setName(name);
		customer.setEmail(email);
		customer.setPhone(customerModel.getPhone());
		return customer;
	}

	private void verifyGetAllMockBehaviour() {
		verify(repository, times(1)).findAll();
		
	}

	private void stubRepositoryGetAll() {
		when(repository.findAll()).thenReturn(List.of(customerModel));
	}

	private void verifyGetByIdMockBehaviour() {
		verify(repository, times(1)).findById(any(Long.class));
	}

	private void stubRepositoryGetById(Optional<Customer> optional) {
		when(repository.findById(any(Long.class))).thenReturn(optional);
	}

	private <T> void verifyExceptionResult(T exception, String message) {
		assertEquals(message, ((Throwable) exception).getMessage());
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
