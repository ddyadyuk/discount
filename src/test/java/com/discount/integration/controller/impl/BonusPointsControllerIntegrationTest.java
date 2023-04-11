package com.discount.integration.controller.impl;

import com.discount.DiscountApplication;
import com.discount.dto.ClientRequestDto;
import com.discount.dto.ReceiptDto;
import com.discount.dto.ReceiptPositionDto;
import com.discount.dto.WithdrawPointsDto;
import com.discount.service.BonusPointsService;
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
class BonusPointsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BonusPointsService bonusPointsService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ReceiptService receiptService;

    @Test
    void withdrawPoints_whenWithdrawPointsDtoIsValid_thenStatus200() throws Exception {
        Long clientId = setupClientWithReceipts(BigDecimal.valueOf(20_000));

        WithdrawPointsDto withdrawPointsDto = getValidWithdrawPointsDto(clientId, BigDecimal.valueOf(100));
        mockMvc.perform(post("/api/points")
                                .content(new ObjectMapper().writeValueAsString(withdrawPointsDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                                   .json("{\"withdrawnMoney\":1000,\"withdrawnPoints\":100,\"availablePoints\":300.00}",
                                         true));
    }

    @Test
    void withdrawPoints_whenWithdrawPointsDtoHasCorrectAmount_thenStatus200() throws Exception {
        Long clientId = setupClientWithReceipts(BigDecimal.valueOf(20_000));

        WithdrawPointsDto withdrawPointsDto = getValidWithdrawPointsDto(clientId, BigDecimal.valueOf(100));
        mockMvc.perform(post("/api/points")
                                .content(new ObjectMapper().writeValueAsString(withdrawPointsDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                                   .json("{\"withdrawnMoney\":1000,\"withdrawnPoints\":100,\"availablePoints\":300.00}",
                                         true));
    }

    @Test
    void withdrawPoints_whenWithdrawPointsDtoHasIncorrectAmount_thenStatus400() throws Exception {
        Long clientId = setupClientWithReceipts(BigDecimal.valueOf(20_000));

        WithdrawPointsDto withdrawPointsDto = getValidWithdrawPointsDto(clientId, BigDecimal.valueOf(500));
        mockMvc.perform(post("/api/points")
                                .content(new ObjectMapper().writeValueAsString(withdrawPointsDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void withdrawPoints_whenWithdrawPointsDtoIsIncorrect_thenStatus422() throws Exception {
        WithdrawPointsDto withdrawPointsDto = new WithdrawPointsDto();

        mockMvc.perform(post("/api/points")
                                .content(new ObjectMapper().writeValueAsString(withdrawPointsDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }

    @Test
    void getAvailablePoints_whenUserIsPresent_thenStatus200() throws Exception {
        Long clientId = setupClientWithReceipts(BigDecimal.valueOf(100_001));

        mockMvc.perform(get("/api/points?clientId={clientId}", clientId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"availablePoints\":3333.00}",
                                          true));
    }

    @Test
    void getAvailablePoints_whenUserIsNotPresent_thenStatus400() throws Exception {
        mockMvc.perform(get("/api/points?clientId={clientId}", 1))
                .andExpect(status().isBadRequest());
    }

    private Long setupClientWithReceipts(BigDecimal receiptTotalGrand) {
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        clientRequestDto.setCardNumber("12345561");
        // Create user
        Long clientId = clientService.save(clientRequestDto);

        ReceiptDto receiptDto = new ReceiptDto();
        receiptDto.setClientId(clientId);
        receiptDto.setReceiptPositions(List.of(new ReceiptPositionDto(receiptTotalGrand)));
        // Add receipts
        receiptService.save(receiptDto);

        return clientId;
    }

    private WithdrawPointsDto getValidWithdrawPointsDto(Long clientId, BigDecimal amount) {
        WithdrawPointsDto withdrawPointsDto = new WithdrawPointsDto();
        withdrawPointsDto.setClientId(clientId);
        withdrawPointsDto.setAmount(amount);

        return withdrawPointsDto;
    }
}