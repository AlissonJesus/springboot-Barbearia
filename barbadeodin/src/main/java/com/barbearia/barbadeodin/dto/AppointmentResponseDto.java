package com.barbearia.barbadeodin.dto;

import java.time.LocalDateTime;

public record AppointmentResponseDto(
		Long id,
		long specialistId,
		long serviceId,
		LocalDateTime date,
		String notes) {

}
