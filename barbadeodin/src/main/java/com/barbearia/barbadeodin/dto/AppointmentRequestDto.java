package com.barbearia.barbadeodin.dto;

import java.time.LocalDateTime;

public record AppointmentRequestDto(
		long specialistId,
		long serviceId,
		LocalDateTime date,
		String notes
		) {

}
