package com.discount.service;

import com.discount.dao.model.ReceiptPosition;
import com.discount.dto.ReceiptPositionDto;

import java.util.List;
import java.util.Set;

public interface ReceiptPositionService {
    List<ReceiptPosition> save(List<ReceiptPositionDto> receiptPositions);
}
