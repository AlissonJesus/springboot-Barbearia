package com.barbearia.barbadeodin.models;

import java.time.LocalDateTime;
import java.util.List;

import com.barbearia.barbadeodin.dto.GroomingServiceDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "GroomingService")
@Table(name = "services")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class GroomingService {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String name;
	String description;
	Double price;
	Integer duration;
	LocalDateTime createdAt;
	
	@ManyToMany(mappedBy = "services")
    List<Specialist> specialists;
	
	public GroomingService(GroomingServiceDto service) {
		name = service.name();
		description = service.description();
		price = service.price();
		duration = service.duration();
	}
	

	public void update(GroomingServiceDto service) {
		name = service.name();
		description = service.description();
		price = service.price();
		duration = service.duration();
	}
	
	
}
