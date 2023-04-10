package com.discount.service.impl;

import com.discount.dao.model.Client;
import com.discount.dao.model.Receipt;
import com.discount.dao.model.ReceiptPosition;
import com.discount.dao.repository.ClientRepository;
import com.discount.dto.BonusPointsResponse;
import com.discount.dto.WithdrawPointsDto;
import com.discount.exception.IncorrectWithdrawalAmountException;
import com.discount.service.ConversionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BonusPointsServiceImplTest {

    public static final int DISCOUNT_POINTS = 300;
    public static final int WITHDRAWN_POINTS = 100;
    public static final int WITHDRAWN_MONEY = WITHDRAWN_POINTS * 10;
    public static final int INCORRECT_WITHDRAWN_POINTS = 500;

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ConversionService conversionService;

    private BonusPointsServiceImpl bonusPointsService;

    @BeforeEach
    public void setup() {
        bonusPointsService = new BonusPointsServiceImpl(clientRepository, conversionService);
    }

    @Test
    void withdrawPoints_whenWithdrawPointsDtoAmountFits_thenReturnBonusPointsResponse() {
        WithdrawPointsDto withdrawPointsDto = getConfiguredWithdrawPointsDto(BigDecimal.valueOf(WITHDRAWN_POINTS));

        Client client = getConfiguredClient(BigDecimal.valueOf(DISCOUNT_POINTS));

        when(clientRepository.findClientById(any())).thenReturn(client);
        when(conversionService.convertToMoney(any())).thenReturn(BigDecimal.valueOf(WITHDRAWN_MONEY));

        BonusPointsResponse bonusPointsResponse = bonusPointsService.withdrawPoints(withdrawPointsDto);

        assertNotNull(bonusPointsResponse);
        assertEquals(BigDecimal.valueOf(DISCOUNT_POINTS).subtract(BigDecimal.valueOf(WITHDRAWN_POINTS)),
                     bonusPointsResponse.getAvailablePoints());
        assertEquals(BigDecimal.valueOf(WITHDRAWN_POINTS), bonusPointsResponse.getWithdrawnPoints());
        assertEquals(BigDecimal.valueOf(WITHDRAWN_MONEY), bonusPointsResponse.getWithdrawnMoney());
    }

    @Test
    void withdrawPoints_whenWithdrawPointsDtoAmountDoesNotFit_thenIncorrectWithdrawalAmountExceptionIsThrown() {
        WithdrawPointsDto withdrawPointsDto = getConfiguredWithdrawPointsDto(
                BigDecimal.valueOf(INCORRECT_WITHDRAWN_POINTS));

        Client client = getConfiguredClient(BigDecimal.valueOf(DISCOUNT_POINTS));

        when(clientRepository.findClientById(any())).thenReturn(client);

        assertThrows(IncorrectWithdrawalAmountException.class,
                     () -> bonusPointsService.withdrawPoints(withdrawPointsDto));
    }

    @Test
    void getAvailablePoints_whenUserIsPresent_thenReturnBonusPointsResponse() {

        Client client = getConfiguredClient(BigDecimal.valueOf(DISCOUNT_POINTS));

        when(clientRepository.findClientById(any())).thenReturn(client);

        BonusPointsResponse availablePoints = bonusPointsService.getAvailablePoints(client.getId());

        assertNotNull(availablePoints);
        assertEquals(client.getDiscountPoints(), availablePoints.getAvailablePoints());
    }

    @Test
    void recalculateBonusPoints_whenClientIsPresent_thenUpdateDiscountPoints() {

        Client client = getConfiguredClient(BigDecimal.ZERO);

        BigDecimal calculatedBonusPoints = BigDecimal.ONE;

        Receipt newReceipt = getConfiguredReceipt(false, BigDecimal.valueOf(20_000));
        client.getReceipts().add(newReceipt);

        when(clientRepository.findClientById(any())).thenReturn(client);
        when(conversionService.convertToBonusPoints(any(), any())).thenReturn(calculatedBonusPoints);

        bonusPointsService.recalculateBonusPoints(1L);

        assertNotNull(client.getDiscountPoints());
        assertEquals(calculatedBonusPoints, client.getDiscountPoints());
        assertEquals(0, client.getReceipts().stream()
                .flatMap(r -> r.getReceiptPositions().stream())
                .filter(rp -> Boolean.FALSE.equals(rp.getIsProcessed()))
                .count());
    }

    private WithdrawPointsDto getConfiguredWithdrawPointsDto(BigDecimal amount) {
        WithdrawPointsDto withdrawPointsDto = new WithdrawPointsDto();
        withdrawPointsDto.setClientId(1L);
        withdrawPointsDto.setAmount(amount);

        return withdrawPointsDto;
    }

    private Client getConfiguredClient(BigDecimal discountPoints) {
        Client client = new Client();
        client.setId(1L);
        client.setDiscountPoints(discountPoints);

        return client;
    }

    private static Receipt getConfiguredReceipt(Boolean isProcessed, BigDecimal amount) {
        ReceiptPosition receiptPosition = new ReceiptPosition();
        receiptPosition.setPrice(amount);
        receiptPosition.setIsProcessed(isProcessed);

        Receipt receipt = new Receipt();
        receipt.setReceiptPositions(List.of(receiptPosition));
        return receipt;
    }

}