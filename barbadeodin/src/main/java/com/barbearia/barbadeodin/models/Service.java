package com.barbearia.barbadeodin.models;

import java.time.LocalDateTime;

import com.barbearia.barbadeodin.dto.ServiceDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Service")
@Table(name = "services")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Service {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String name;
	String description;
	Double price;
	Integer duration;
	LocalDateTime createdAt;
	
	public Service(ServiceDto service) {
		name = service.name();
		description = service.description();
		price = service.price();
		duration = service.duration();
	}
	
	
}
