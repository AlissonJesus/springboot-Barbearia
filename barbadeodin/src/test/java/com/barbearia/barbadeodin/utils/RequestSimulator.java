package com.barbearia.barbadeodin.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@Component
public class RequestSimulator {
	@Autowired
    private MockMvc mvc;

    public MockHttpServletResponse simulateGetRequest(String urlTemplate) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");
        return executeRequest(requestBuilder);
    }
    
    public MockHttpServletResponse simulateGetRequest(String urlTemplate, Long id) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get(urlTemplate, id)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");
        return executeRequest(requestBuilder);
    }

    public MockHttpServletResponse simulatePostRequest(String urlTemplate, String content) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(content);
        return executeRequest(requestBuilder);
    }

    public MockHttpServletResponse simulatePutRequest(String urlTemplate, Long id, String content) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = put(urlTemplate, id)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(content);
        return executeRequest(requestBuilder);
    }

    public MockHttpServletResponse simulateDeleteRequest(String urlTemplate, Long id) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = delete(urlTemplate, id)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");
        return executeRequest(requestBuilder);
    }

    private MockHttpServletResponse executeRequest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return mvc.perform(requestBuilder).andReturn().getResponse();
    }
}
