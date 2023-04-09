package com.discount.service.impl;

import com.discount.dao.model.ReceiptPosition;
import com.discount.dao.repository.ReceiptPositionRepository;
import com.discount.dto.ReceiptPositionDto;
import com.discount.service.ReceiptPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ReceiptPositionServiceImpl implements ReceiptPositionService {

    private final ReceiptPositionRepository receiptPositionRepository;

    @Override
    public List<ReceiptPosition> save(List<ReceiptPositionDto> receiptPositions) {
        return receiptPositionRepository.saveAll(receiptPositions.stream()
                                                         .map(this::mapToReceiptPosition)
                                                         .collect(Collectors.toSet()));
    }

    private ReceiptPosition mapToReceiptPosition(ReceiptPositionDto rp) {
        ReceiptPosition receiptPosition = new ReceiptPosition();

        receiptPosition.setPrice(rp.getAmount());
        receiptPosition.setIsProcessed(Boolean.FALSE);

        return receiptPosition;
    }
}
