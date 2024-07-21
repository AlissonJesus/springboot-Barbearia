package com.barbearia.barbadeodin.integrations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

import com.barbearia.barbadeodin.dto.GroomingServiceDetailsDto;
import com.barbearia.barbadeodin.dto.GroomingServiceDto;

import com.barbearia.barbadeodin.repositories.GroomingServiceRepository;
import com.barbearia.barbadeodin.utils.RequestSimulator;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class GroomingServiceIntegrationTest {

	

}
