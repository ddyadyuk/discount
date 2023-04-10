package com.discount.service.impl;

import com.discount.dao.model.Client;
import com.discount.dao.model.ReceiptPosition;
import com.discount.dao.repository.ClientRepository;
import com.discount.dto.BonusPointsResponse;
import com.discount.dto.WithdrawPointsDto;
import com.discount.exception.IncorrectWithdrawalAmountException;
import com.discount.service.BonusPointsService;
import com.discount.service.ConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BonusPointsServiceImpl implements BonusPointsService {

    private final ClientRepository clientRepository;
    private final ConversionService conversionService;


    @Override
    public BonusPointsResponse withdrawPoints(WithdrawPointsDto withdrawPointsDto) {
        Client client = clientRepository.findClientById(withdrawPointsDto.getClientId());

        verifyWithdrawalAmount(client.getDiscountPoints(), withdrawPointsDto.getAmount());

        client.setDiscountPoints(client.getDiscountPoints().subtract(withdrawPointsDto.getAmount()));

        BigDecimal moneyToWithdraw = conversionService.convertToMoney(withdrawPointsDto.getAmount());

        //todo: add mapper
        BonusPointsResponse bonusPointsResponse = new BonusPointsResponse();
        bonusPointsResponse.setWithdrawnPoints(withdrawPointsDto.getAmount());
        bonusPointsResponse.setWithdrawnMoney(moneyToWithdraw);
        bonusPointsResponse.setAvailablePoints(client.getDiscountPoints());

        return bonusPointsResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public BonusPointsResponse getAvailablePoints(Long clientId) {
        Client clientModel = clientRepository.findClientById(clientId);

        //todo: add mapper
        BonusPointsResponse bonusPointsResponse = new BonusPointsResponse();
        bonusPointsResponse.setAvailablePoints(clientModel.getDiscountPoints());

        return bonusPointsResponse;
    }

    @Override
    public void recalculateBonusPoints(Long clientId) {
        Client client = clientRepository.findClientById(clientId);

        // Get unprocessed money amount that should be recalculated and added to the bonus points
        BigDecimal unprocessedClientReceiptAmount = client.getReceipts().stream()
                .flatMap(r -> r.getReceiptPositions().stream())
                .filter(rp -> Boolean.FALSE.equals(rp.getIsProcessed()))
                .map(ReceiptPosition::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Get grand total amount for all Receipts
        BigDecimal receiptsGrandTotal = client.getReceipts().stream()
                .flatMap(r -> r.getReceiptPositions().stream())
                .map(ReceiptPosition::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate earned discount points based on the unprocessed Receipt Positions amounts
        BigDecimal discountPointsToAdd =
                conversionService.convertToBonusPoints(receiptsGrandTotal, unprocessedClientReceiptAmount);

        client.setDiscountPoints(client.getDiscountPoints().add(discountPointsToAdd));

        client.getReceipts().stream().flatMap(r -> r.getReceiptPositions().stream())
                .filter(rp -> Boolean.FALSE.equals(rp.getIsProcessed()))
                .forEach(rp -> rp.setIsProcessed(Boolean.TRUE));
    }

    private void verifyWithdrawalAmount(BigDecimal discountPoints, BigDecimal amount) {
        if (discountPoints.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            log.warn("Specified withdrawal amount: {} is greater that available amount: {} ", amount, discountPoints);

            throw new IncorrectWithdrawalAmountException(
                    String.format("Withdrawal amount is too high, available %s discount points", discountPoints));
        }
    }

}
