package com.barbearia.barbadeodin.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.barbearia.barbadeodin.dto.ServiceDetailsDto;
import com.barbearia.barbadeodin.models.ServiceDto;
import com.barbearia.barbadeodin.services.ServiceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ServiceControllerTest {
	
	@MockBean
	private ServiceService service;
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
    private ObjectMapper mapper;
	
	@Autowired
	private JacksonTester<ServiceDto> payloadJson;
	
	private ServiceDto payload = new ServiceDto(
			"Corte", 
			"Corte de Cabelo Profissional a pedido do cliente, finalizando no lavatório com Shampoo especializado e penteado ao final!",
			25, 30);
	
	private ServiceDetailsDto serviceDetalhado = new ServiceDetailsDto(
			1L, 
			payload.name(), 
			payload.Description(), 
			payload.price(), 
			payload.duration());
	
	@Test
	void deveCadastrarComSucessoRetornaStatus200() throws Exception {
		preparaCenarioSimulacaoCadastra();
		var response = executaSimulacaoCadastra();
		verificaRespostaEsperada(response);
		verificaComportamentoMock();
	}

	private void verificaComportamentoMock() {
		verify(service, times(1)).cadastra(payload);	
	}

	private void verificaRespostaEsperada(MockHttpServletResponse response) throws Exception {
		var body = response.getContentAsString(StandardCharsets.UTF_8);
		
		verifyStatus(response.getStatus(), HttpStatus.CREATED);
		
		verifyBody(body);
	}

	private void verifyBody(String body) throws JsonMappingException, JsonProcessingException {
		JsonNode rootNode = mapper.readTree(body);
		
		Long id = rootNode.path("id").asLong();
		String name = rootNode.path("name").asText();
		String description = rootNode.path("description").asText();
        double price = rootNode.path("price").asDouble();
        int duration = rootNode.path("duration").asInt();
        
        assertThat(id).isEqualTo(serviceDetalhado.id());
        assertThat(name).isEqualTo(serviceDetalhado.name());
        assertThat(description).isEqualTo(serviceDetalhado.description());
        assertThat(price).isEqualTo(serviceDetalhado.price());
        assertThat(duration).isEqualTo(serviceDetalhado.duration());
	}

	private void verifyStatus(int status, HttpStatus type) {		
		assertThat(status).isEqualTo(type.value());
		
	}

	private MockHttpServletResponse executaSimulacaoCadastra() throws Exception {
		
		var jsonEsperado = criaJsonEsperado(payload, payloadJson);
		
		return mvc.perform(post("/services")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonEsperado)
				.characterEncoding("UTF-8"))
				.andReturn().getResponse();
	}
	
	private <T> String criaJsonEsperado(T payload, JacksonTester<T> payloadJson) throws IOException {
		return payloadJson.write(payload).getJson();
	}

	private void preparaCenarioSimulacaoCadastra() {
		when(service.cadastra(payload)).thenReturn(serviceDetalhado);
	}
}
