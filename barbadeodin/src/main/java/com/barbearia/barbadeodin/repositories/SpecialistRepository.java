package com.barbearia.barbadeodin.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.barbearia.barbadeodin.models.Specialist;

public interface SpecialistRepository extends JpaRepository<Specialist, Long> {
	@Query("SELECT s.id as id, s.name as name, s.imagemUrl as imagemUrl, " +
	           "s.services as services FROM Specialist s")
	List<SpecialistProjection> findAllWithServiceIds();

	@Query("SELECT s.id as id, s.name as name, s.imagemUrl as imagemUrl, " +
	           "s.services as services FROM Specialist s WHERE s.id = :id")
	    Optional<SpecialistProjection> findSpecialistById(Long id);
}