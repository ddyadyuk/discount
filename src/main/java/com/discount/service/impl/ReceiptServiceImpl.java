package com.discount.service.impl;

import com.discount.dao.model.Client;
import com.discount.dao.model.Receipt;
import com.discount.dao.model.ReceiptPosition;
import com.discount.dao.repository.ClientRepository;
import com.discount.dao.repository.ReceiptRepository;
import com.discount.dto.ReceiptDto;
import com.discount.dto.ReceiptPositionDto;
import com.discount.service.BonusPointsService;
import com.discount.service.ReceiptPositionService;
import com.discount.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
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

    @Override
    public void save(ReceiptDto receiptDto) {
        Client client = clientRepository.findClientById(receiptDto.getClientId());

        Receipt receiptModel = new Receipt();
        // link together receipt and receipt positions
        receiptModel.setReceiptPositions(receiptPositionService.save(receiptDto.getReceiptPositions()));
        // link together receipt and client
        client.addReceipt(receiptModel);
        //Save the Receipt
        receiptRepository.saveAndFlush(receiptModel);
        //Recalculate user bonus points
        bonusPointsService.recalculateBonusPoints(client.getId());
    }

    @Override
    public Set<ReceiptDto> getReceiptsByClientId(Long clientId) {
        return receiptRepository.findReceiptsByClientId(clientId).stream()
                .map(this::mapToReceiptDto)
                .collect(Collectors.toSet());
    }

    private ReceiptDto mapToReceiptDto(Receipt receipt) {
        ReceiptDto receiptDto = new ReceiptDto();
        receiptDto.setClientId(receipt.getClient().getId());

        List<ReceiptPositionDto> receiptPositionDtos = new ArrayList<>();
        receipt.getReceiptPositions().stream()
                .map(ReceiptPosition::getPrice)
                .forEach(rp -> receiptPositionDtos.add(new ReceiptPositionDto(rp)));

        receiptDto.setReceiptPositions(receiptPositionDtos);

        return receiptDto;
    }
}
