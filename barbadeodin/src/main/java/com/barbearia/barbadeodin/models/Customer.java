package com.barbearia.barbadeodin.models;

import com.barbearia.barbadeodin.dto.CustomerRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Customer")
@Table(name = "customers")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String phone;
	private String email;
	
	public Customer(CustomerRequestDto requestBody) {
		name = requestBody.name();
		phone = requestBody.phone();
		email = requestBody.email();
	}
	
	public void setId(Long id) {
		this.id = id;
	}
}

