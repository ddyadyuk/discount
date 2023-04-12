package com.discount.service;

import com.discount.dao.model.ReceiptPosition;
import com.discount.dto.ReceiptPositionDto;

public interface ReceiptPositionService {
    ReceiptPosition save(ReceiptPositionDto receiptPosition);

}
