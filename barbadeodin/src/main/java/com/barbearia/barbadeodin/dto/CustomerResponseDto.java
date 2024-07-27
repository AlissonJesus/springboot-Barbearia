package com.barbearia.barbadeodin.dto;

import com.barbearia.barbadeodin.models.Customer;

public record CustomerResponseDto(
		Long id,
		String name,
		String phone,
		String email) {

	public CustomerResponseDto(Customer model) {
		this(model.getId(), model.getName(), model.getPhone(), model.getEmail());
	}
}
