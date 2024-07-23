package com.barbearia.barbadeodin.models;


import java.util.List;

import com.barbearia.barbadeodin.dto.SpecialistDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Specialist")
@Table(name = "specialists")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Specialist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String name;
	String imagemUrl;
	
	@ManyToMany
    @JoinTable(
        name = "specialist_services",
        joinColumns = @JoinColumn(name = "specialist_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
	List<GroomingService> services;
	
	public Specialist(SpecialistDto payload, List<GroomingService> serviceList) {
		name = payload.name();
		imagemUrl = payload.imagemUrl();
		services = serviceList;
	}

	public void setId(Long id) {
		this.id = id;
		
	}
	

	
}
