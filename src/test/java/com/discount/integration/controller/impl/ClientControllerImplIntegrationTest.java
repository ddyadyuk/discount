package com.discount.integration.controller.impl;

import com.discount.DiscountApplication;
import com.discount.dto.ClientRequestDto;
import com.discount.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("integration-test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = WebEnvironment.MOCK,
        classes = DiscountApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(
        locations = "classpath:application-integrationtest.yml")
class ClientControllerImplIntegrationTest {

    public static final int CLIENT_ID = 1;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ClientService clientService;

    @Test
    void addClient_whenClientRequestDtoIsValid_thenStatus200() throws Exception {
        ClientRequestDto clientRequestDto = getValidClientRequestDto();
        mvc.perform(post("/api/clients")
                            .content(new ObjectMapper().writeValueAsString(clientRequestDto))
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk());
    }

    @Test
    void addClient_whenClientRequestDtoIsNotValid_thenStatus422() throws Exception {
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        mvc.perform(post("/api/clients")
                            .content(new ObjectMapper().writeValueAsString(clientRequestDto))
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }

    @Test
    void removeClient_whetClientIsPresent_thenStatus200() throws Exception {
        ClientRequestDto clientRequestDto = getValidClientRequestDto();

        clientService.save(clientRequestDto);

        mvc.perform(delete("/api/clients/{clientId}", CLIENT_ID))
                .andExpectAll(status().isOk());
    }

    @Test
    void removeClient_whetClientIsNotPresent_thenStatus400() throws Exception {
        mvc.perform(delete("/api/clients/" + CLIENT_ID))
                .andExpectAll(status().isOk());
    }

    private ClientRequestDto getValidClientRequestDto() {
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        clientRequestDto.setCardNumber("123214124");

        return clientRequestDto;
    }
}