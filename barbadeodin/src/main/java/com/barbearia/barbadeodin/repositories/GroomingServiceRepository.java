package com.barbearia.barbadeodin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.barbearia.barbadeodin.models.GroomingService;

public interface GroomingServiceRepository extends JpaRepository<GroomingService, Long>{

}
