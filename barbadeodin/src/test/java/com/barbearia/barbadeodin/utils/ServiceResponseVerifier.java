package com.barbearia.barbadeodin.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;

import com.barbearia.barbadeodin.dto.GroomingServiceDetailsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ServiceResponseVerifier {
	
	private static	ObjectMapper mapper = new ObjectMapper();
	
    public static void verifyExpectedResponseRegister(MockHttpServletResponse response, HttpStatus expectedStatus, GroomingServiceDetailsDto expectedBody) throws Exception {
    	verifyStatus(response.getStatus(), expectedStatus);
		var body = response.getContentAsString(StandardCharsets.UTF_8);
		JsonNode rootNode = mapper.readTree(body);
		verifyBody(rootNode, expectedBody);
    }
    
    private static void verifyBody(JsonNode rootNode, GroomingServiceDetailsDto expectedBody) throws JsonMappingException, JsonProcessingException {	
		Long id = rootNode.path("id").asLong();
		String name = rootNode.path("name").asText();
		String description = rootNode.path("description").asText();
        double price = rootNode.path("price").asDouble();
        int duration = rootNode.path("duration").asInt();
        
        assertThat(id).isEqualTo(expectedBody.id());
        assertThat(name).isEqualTo(expectedBody.name());
        assertThat(description).isEqualTo(expectedBody.description());
        assertThat(price).isEqualTo(expectedBody.price());
        assertThat(duration).isEqualTo(expectedBody.duration());
	}
    
    
	public static void verifyErrorResponse(MockHttpServletResponse response, HttpStatus expectedStatus, String message) throws Exception {
		verifyStatus(response.getStatus(), expectedStatus);
		verifyMessage(response, message);
	}
	
	private static void verifyStatus(int status, HttpStatus expectedStatus) {		
		assertThat(status).isEqualTo(expectedStatus.value());
	}


	private static void verifyMessage(MockHttpServletResponse response, String message) throws Exception {
		var body = response.getContentAsString(StandardCharsets.UTF_8);
		assertThat(body).isEqualTo(message);
		
	}

	public static void verifyErrorResponseGetById(MockHttpServletResponse response, HttpStatus expectedStatus,
			GroomingServiceDetailsDto expectedBody) throws Exception {
		verifyStatus(response.getStatus(), expectedStatus);
		var body = response.getContentAsString(StandardCharsets.UTF_8);
		JsonNode rootNode = mapper.readTree(body);
		verifyBody(rootNode, expectedBody);
		
	}

	public static void verifyExpectedResponseGetAll(MockHttpServletResponse response, HttpStatus ok,
			GroomingServiceDetailsDto expectedBody) throws Exception {
		verifyStatus(response.getStatus(), HttpStatus.OK);
		var body = response.getContentAsString(StandardCharsets.UTF_8);
		JsonNode rootNode = mapper.readTree(body).get(0);
		verifyBody(rootNode, expectedBody);
		
	}

	public static void verifyExpectedResponseDeleteById(MockHttpServletResponse response, HttpStatus expectedStatus) {
		verifyStatus(response.getStatus(), expectedStatus);
	}
}
