package com.discount.service.impl;

import com.discount.dao.model.ReceiptPosition;
import com.discount.dao.repository.ReceiptPositionRepository;
import com.discount.dto.ReceiptPositionDto;
import com.discount.service.ReceiptPositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ReceiptPositionServiceImpl implements ReceiptPositionService {

    private final ReceiptPositionRepository receiptPositionRepository;

    @Override
    public ReceiptPosition save(ReceiptPositionDto receiptPosition) {
        log.info("Saving receipt position = [{}]", receiptPosition);
        ReceiptPosition model = mapToReceiptPosition(receiptPosition);

        return receiptPositionRepository.save(model);
    }

    // todo: due to short amount of time mapping logic was put in here
    // ideally we should use libraries like MapStruct to do the mapping
    private ReceiptPosition mapToReceiptPosition(ReceiptPositionDto rp) {
        ReceiptPosition receiptPosition = new ReceiptPosition();

        receiptPosition.setPrice(rp.getAmount());

        return receiptPosition;
    }
}
