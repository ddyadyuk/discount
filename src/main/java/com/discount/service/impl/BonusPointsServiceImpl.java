package com.discount.service.impl;

import com.discount.dao.model.Client;
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
        log.info("Withdrawing points for client id = [{}] and amount = [{}]", withdrawPointsDto.getClientId(),
                 withdrawPointsDto.getAmount());
        Client client = clientRepository.findClientById(withdrawPointsDto.getClientId());

        verifyWithdrawalAmount(client.getDiscountPoints(), withdrawPointsDto.getAmount());

        client.setDiscountPoints(client.getDiscountPoints().subtract(withdrawPointsDto.getAmount()));

        BigDecimal moneyToWithdraw = conversionService.convertToMoney(withdrawPointsDto.getAmount());

        BonusPointsResponse bonusPointsResponse = new BonusPointsResponse();
        bonusPointsResponse.setWithdrawnPoints(withdrawPointsDto.getAmount());
        bonusPointsResponse.setWithdrawnMoney(moneyToWithdraw);
        bonusPointsResponse.setAvailablePoints(client.getDiscountPoints());

        return bonusPointsResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public BonusPointsResponse getAvailablePoints(Long clientId) {
        log.info("Getting available points for client id = [{}]", clientId);
        Client clientModel = clientRepository.findClientById(clientId);

        BonusPointsResponse bonusPointsResponse = new BonusPointsResponse();
        bonusPointsResponse.setAvailablePoints(clientModel.getDiscountPoints());

        return bonusPointsResponse;
    }

    @Override
    public void recalculateBonusPoints(Client client, BigDecimal receiptTotal) {
        log.info("Recalculating bonus points for client id = [{}]", client.getId());

        BigDecimal discountPointsToAdd =
                conversionService.convertToBonusPoints(client.getGrandTotal(), receiptTotal);

        client.setDiscountPoints(client.getDiscountPoints().add(discountPointsToAdd));
        log.info("User with id {} has {} points", client.getId(), client.getDiscountPoints());
    }

    private void verifyWithdrawalAmount(BigDecimal discountPoints, BigDecimal amount) {
        if (discountPoints.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            log.warn("Specified withdrawal amount: {} is greater that available amount: {} ", amount, discountPoints);

            throw new IncorrectWithdrawalAmountException(
                    String.format("Withdrawal amount is too high, available %s discount points", discountPoints));
        }
    }

}
