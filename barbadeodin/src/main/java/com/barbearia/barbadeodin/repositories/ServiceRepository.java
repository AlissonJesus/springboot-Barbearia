package com.barbearia.barbadeodin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barbearia.barbadeodin.models.Service;

public interface ServiceRepository extends JpaRepository<Service, Long>{

}
