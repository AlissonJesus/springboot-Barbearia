package com.barbearia.barbadeodin.repositories;

import java.util.List;

public interface SpecialistProjection {
	Long getId();
    String getName();
    String getImagemUrl();
    List<ServiceIdOnly> getServices();
}
