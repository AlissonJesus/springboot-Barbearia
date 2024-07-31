package com.barbearia.barbadeodin.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barbearia.barbadeodin.dto.CustomerRequestDto;
import com.barbearia.barbadeodin.dto.CustomerResponseDto;
import com.barbearia.barbadeodin.exceptions.EmailAlreadyExistsException;
import com.barbearia.barbadeodin.models.Customer;
import com.barbearia.barbadeodin.repositories.CustomerRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository repository;

	public CustomerResponseDto register(CustomerRequestDto requestBody) {
		checkEmailAlreadyExists(requestBody);
		Customer customer = new Customer(requestBody);
		customer = repository.save(customer);
		return createCustomerResponseDto(customer);
	}


	public CustomerResponseDto getById(Long id) {
		return createCustomerResponseDto(getCustomer(id));		
	}


	public List<CustomerResponseDto> getAll() {
		return repository.findAll()
				.stream()
				.map(CustomerResponseDto::new)
				.toList();
	}

	public CustomerResponseDto updateById(Long id, CustomerRequestDto requestBody) {
		Customer existingCustomer = repository.getReferenceById(id);
		checkEmailAlreadyExists(existingCustomer, requestBody);
		existingCustomer.updateFields(requestBody);
		return createCustomerResponseDto(existingCustomer);
	}

	public void deleteById(Long id) {
		repository.deleteById(id);	
	}
	
	private Customer getCustomer(Long id) {
		var existingCustomer = repository.findById(id);
		if(existingCustomer.isEmpty())throw new EntityNotFoundException("Cliente não encontrado");
		return existingCustomer.get();
	}
	
	private void checkEmailAlreadyExists(Customer existingCustomer, CustomerRequestDto requestBody) {
		var emailNoExists = !repository.existsByEmail(requestBody.email());	
		if(emailNoExists) return;
		if(existingCustomer.getEmail() == requestBody.email()) return;
		throw new EmailAlreadyExistsException("Email já cadastrado");
	}
	
	private void checkEmailAlreadyExists(CustomerRequestDto requestBody) {
		var emailExists = repository.existsByEmail(requestBody.email());
		if(emailExists) throw new EmailAlreadyExistsException("Email já cadastrado");
	}
	
	private CustomerResponseDto createCustomerResponseDto(Customer customer) {
		return new CustomerResponseDto(customer);
	}
	

}
