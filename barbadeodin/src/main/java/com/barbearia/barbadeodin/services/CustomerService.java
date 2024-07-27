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
		// TODO Auto-generated method stub
		return null;
	}

	public CustomerResponseDto updateById(Long id, CustomerRequestDto requestBody) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteById(Long any) {
		// TODO Auto-generated method stub
		
	}
	
	private Customer getCustomer(Long id) {
		var existingCustomer = repository.findById(id);
		if(existingCustomer.isEmpty())throw new EntityNotFoundException("Cliente não encontrado");
		return existingCustomer.get();
	}
	
	private void checkEmailAlreadyExists(CustomerRequestDto requestBody) {
		var emailExists = repository.existsByEmail(requestBody.email());
		if(emailExists) throw new EmailAlreadyExistsException("Email já cadastrado");
	}
	
	private CustomerResponseDto createCustomerResponseDto(Customer customer) {
		return new CustomerResponseDto(customer);
	}
	

}
