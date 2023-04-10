package com.discount.integration.controller.impl;

import com.discount.DiscountApplication;
import com.discount.dto.ClientRequestDto;
import com.discount.dto.ReceiptDto;
import com.discount.dto.ReceiptPositionDto;
import com.discount.service.ClientService;
import com.discount.service.ReceiptService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
class ReceiptControllerImplIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ReceiptService receiptService;

    @Test
    void addNewReceipt_whenClientRequestDtoIsValid_thenStatus200() throws Exception {
        Long clientId = setupClient();
        ReceiptDto receiptDto = getValidReceiptDto(clientId, BigDecimal.valueOf(20_000));

        mockMvc.perform(post("/api/receipts")
                                .content(new ObjectMapper().writeValueAsString(receiptDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk());
    }

    @Test
    void addNewReceipt_whenClientRequestDtoIsNotValid_thenStatus422() throws Exception {
        mockMvc.perform(post("/api/receipts")
                                .content(new ObjectMapper().writeValueAsString(new ReceiptDto()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }

    @Test
    void getReceiptsByClientId_whenClientIsPresent_thenStatus200() throws Exception {
        Long clientId = setupClientWithReceipts(BigDecimal.valueOf(55_000));

        mockMvc.perform(get("/api/receipts?clientId={clientId}", clientId))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"clientId\":1,\"receiptPositions\":[{\"amount\":55000.00}]}]"));
    }

    @Test
    void getReceiptsByClientId_whenClientIsNotPresent_thenStatus200() throws Exception {
        mockMvc.perform(get("/api/receipts?clientId={clientId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    private ReceiptDto getValidReceiptDto(Long clientId, BigDecimal receiptPositionAmount) {
        ReceiptPositionDto receiptPositionDto = new ReceiptPositionDto();
        receiptPositionDto.setAmount(receiptPositionAmount);

        ReceiptDto receiptDto = new ReceiptDto();
        receiptDto.setClientId(clientId);
        receiptDto.setReceiptPositions(List.of(receiptPositionDto));

        return receiptDto;
    }

    private Long setupClient() {
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        clientRequestDto.setCardNumber("12345561");

        return clientService.save(clientRequestDto);
    }

    private Long setupClientWithReceipts(BigDecimal receiptTotalGrand) {
        Long clientId = setupClient();
        ReceiptDto receiptDto = new ReceiptDto();
        receiptDto.setClientId(clientId);
        receiptDto.setReceiptPositions(List.of(new ReceiptPositionDto(receiptTotalGrand)));

        receiptService.save(receiptDto);

        return clientId;
    }
}