package com.barbearia.barbadeodin.dto;

public record CustomerResponseDto(
		Long id,
		String name,
		String phone,
		String email) {
}
