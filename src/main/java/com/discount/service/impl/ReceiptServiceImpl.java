package com.discount.service.impl;

import com.discount.dao.model.Client;
import com.discount.dao.model.Receipt;
import com.discount.dao.model.ReceiptPosition;
import com.discount.dao.repository.ClientRepository;
import com.discount.dao.repository.ReceiptRepository;
import com.discount.dto.ReceiptDto;
import com.discount.dto.ReceiptPositionDto;
import com.discount.dto.ReceiptRequestDto;
import com.discount.service.BonusPointsService;
import com.discount.service.ReceiptPositionService;
import com.discount.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class ReceiptServiceImpl implements ReceiptService {
    private final ClientRepository clientRepository;
    private final ReceiptRepository receiptRepository;
    private final ReceiptPositionService receiptPositionService;
    private final BonusPointsService bonusPointsService;
    private final ConversionServiceImpl conversionService;

    @Override
    @Transactional
    public void save(ReceiptRequestDto receiptDto) {
        log.info("Saving new receipt = [{}]", receiptDto);

        Client client = clientRepository.findClientById(receiptDto.getClientId());
        Receipt receiptModel = new Receipt();
        client.addReceipt(receiptModel);

        receiptDto.getReceiptPositions()
                .stream()
                .map(receiptPositionService::save)
                .forEach(receiptModel::addPosition);

        receiptRepository.save(receiptModel);

        BigDecimal receiptTotal = receiptModel.getReceiptPositions()
                .stream()
                .map(ReceiptPosition::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        client.setGrandTotal(client.getGrandTotal().add(receiptTotal));

        bonusPointsService.recalculateBonusPoints(client, receiptTotal);
    }

    @Override
    public Set<ReceiptDto> getReceiptsByClientId(Long clientId) {
        log.info("Getting receipts by client id = [{}]", clientId);

        return receiptRepository.findReceiptsByClientId(clientId).stream()
                .map(this::mapToReceiptDto)
                .collect(Collectors.toSet());
    }

    private ReceiptDto mapToReceiptDto(Receipt receipt) {
        ReceiptDto receiptDto = new ReceiptDto();

        List<ReceiptPositionDto> receiptPositionDtos = receipt.getReceiptPositions().stream()
                .map(ReceiptPosition::getPrice)
                .map(ReceiptPositionDto::new)
                .collect(Collectors.toList());

        receiptDto.setReceiptPositions(receiptPositionDtos);

        return receiptDto;
    }
}
